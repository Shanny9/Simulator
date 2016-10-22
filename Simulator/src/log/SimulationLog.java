package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.SimulationTime;
import utils.SolutionElement;

import com.daoImpl.TblServiceDaoImpl;
import com.model.TblService;

public class SimulationLog extends Thread implements Serializable {

	public static final boolean MAROM = true;
	public static final boolean RAKIA = false;
	private static boolean isInitialized = false;

	private static final long serialVersionUID = 1L;
	/**
	 * The incidents (key=start_time, value= HashSet of ci_id)
	 */
	private HashMap<SimulationTime, HashSet<Byte>> time_cis;
	/**
	 * The incidents (key=ci_id, value= HashSet of SimulationTime)
	 */
	private HashMap<Byte, HashSet<SimulationTime>> cis_time;
	/**
	 * The simulation's map of CIs and their affected services (key=ci_id,
	 * value=set of affected services)
	 */
	private HashMap<Byte, HashSet<Byte>> affecting_cis;
	/**
	 * The simulation's map of services and their affecting CIs (key=service_id,
	 * value=set of affecting CIs)
	 */
	private HashMap<Byte, HashSet<Byte>> affected_services;
	/**
	 * The simulation's incidents and their events (key = time, value=set of
	 * events)
	 */
	private HashMap<SimulationTime, HashSet<String>> time_events;

	/**
	 * The simulation's CI price list (key=ci_id, value=solution cost)
	 */
	private HashMap<Byte, Double> ciSolCosts;

	private HashMap<Integer, Byte> question_ci;
	/**
	 * The simulation's live queue of current solutions
	 */
	private LinkedList<SolutionLog> solutionQueue = new LinkedList<>();
	/**
	 * The simulation's history solutions
	 */
	private Set<SolutionLog> solutionHistory = new HashSet<>();
	/**
	 * The current status of the simulation at the server
	 */
	private static boolean serverPaused = false;
	/**
	 * The current status of the simulation at the client
	 */
	private static boolean clientPaused = false;
	/**
	 * The current round
	 */
	private int round;
	/**
	 * The log of team Marom
	 */
	private TeamLog marom;
	/**
	 * The log of team Rakia
	 */
	private TeamLog rakia;
	/**
	 * The Simulation log's instance
	 */
	private static SimulationLog instance;

	private Settings settings;

	public void initialize(Settings _settings) {

		if (settings == null) {
			settings = _settings;
		}
		if (affecting_cis == null) {
			affecting_cis = LogUtils.getDBAffectingCIs();
		}
		if (affected_services == null) {
			affected_services = LogUtils.getDBAffectedServices();
		}
		if (ciSolCosts == null) {
			ciSolCosts = LogUtils.getCISolCosts();
		}
		if (time_cis == null) {
			time_cis = LogUtils.getTimeCis();
		}
		if (cis_time == null) {
			cis_time = LogUtils.getCisTime();
		}

		if (question_ci == null) {
			question_ci = LogUtils.getQuestionsCis();
		}
		if (marom == null || rakia == null) {
			List<TblService> services = new TblServiceDaoImpl()
					.getAllActiveServices();
			HashMap<Byte, Double> serviceDownTimeCosts = LogUtils
					.getServiceDownTimeCosts();
			HashMap<Byte, ServiceLog> service_logs = new HashMap<>();

			double initDiff = 0;

			// initializes service logs \\TODO: move to LogUtis
			if (services != null) {
				for (TblService service : services) {
					byte service_id = service.getServiceId();
					service_logs.put(service_id, new ServiceLog(service_id,
							service.getFixedCost(), service.getFixedIncome(),
							serviceDownTimeCosts.get(service_id)));
					initDiff += service_logs.get(service_id).getDiff();
				}
			}

			@SuppressWarnings("unchecked")
			HashMap<Byte, ServiceLog> service_logs_copy = (HashMap<Byte, ServiceLog>) LogUtils
					.copy(service_logs);

			marom = new TeamLog("Marom", service_logs, initDiff);
			rakia = new TeamLog("Rakia", service_logs_copy, initDiff);

			isInitialized = true;
			System.out.println("SimulationLog: SimulationLog is initalized.");
		}
	}

	public static boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * Sets he round of the service log, updates the round of the team logs.
	 * 
	 * @param currentRound
	 *            The current round.
	 */
	public void setRound(int currentRound) {
		round = currentRound;
		
//		if (time_events == null) {
			time_events = LogUtils.getRoundEvents(currentRound);
//		}
		
		marom.setRound(currentRound);
		rakia.setRound(currentRound);

		// initializes incident logs
		HashMap<Byte, IncidentLog> incident_logs_current_round = LogUtils
				.getIncidentLogsOfRound(round);

		@SuppressWarnings("unchecked")
		HashMap<Byte, IncidentLog> incident_logs_current_round_copy = (HashMap<Byte, IncidentLog>) LogUtils
				.copy(incident_logs_current_round);

		marom.setIncidentLogs(incident_logs_current_round);
		rakia.setIncidentLogs(incident_logs_current_round_copy);

		System.out.println("SimulationLog: round is set to " + currentRound
				+ ".");
	}

	/**
	 * Empty constructor
	 */
	private SimulationLog() {
	};

	/**
	 * Singleton method
	 * 
	 * @return Instance of the {@code SimulationLog}.
	 */
	public static SimulationLog getInstance() {
		if (instance == null) {
			instance = new SimulationLog();
		}
		return instance;
	}

	/**
	 * @param team
	 *            The team's name
	 * @return The team's log
	 */
	public TeamLog getTeam(boolean team) {
		if (team == MAROM) {
			return marom;
		}
		return rakia;
	}

	public HashMap<String, Double> getTeamProfits(SimulationTime time) {
		HashMap<String, Double> profits = new HashMap<>();
		profits.put(marom.getTeamName(), marom.getProfit(time));
		profits.put(rakia.getTeamName(), rakia.getProfit(time));
		return profits;
	}

	public HashMap<String, Double> getTeamScores(SimulationTime time) {
		HashMap<String, Double> profits = new HashMap<>();

		if (round == 0) {
			System.err
					.println("SimulationLog: If I'm printed there is a thread problem");
			profits.put("Marom", (double) 0);
			profits.put("Rakia", (double) 0);
			return profits;
		}
		try {
			int targetScore = settings.getTargetScores().get(round - 1);
			int initCapital = (int) settings.getInitCapital();
			// TODO: prevent targetWithoughtInit from being negative/zero

			double maromWithoutInit = marom.getProfit(time) - initCapital;
			double rakiaWithoutInit = rakia.getProfit(time) - initCapital;

			if (maromWithoutInit < 0) {
				maromWithoutInit = 0;
			}

			if (rakiaWithoutInit < 0) {
				rakiaWithoutInit = 0;
			}

			profits.put(marom.getTeamName(), maromWithoutInit / targetScore
					* 100);
			profits.put(rakia.getTeamName(), rakiaWithoutInit / targetScore
					* 100);
			// System.out.println("Marom: " + marom.getProfit(time) +
			// ". Rakia: " +
			// rakia.getProfit(time));
			// System.out.println("Marom: " + maromWithoutInit / targetScore *
			// 100 +
			// ". Rakia: " + rakiaWithoutInit / targetScore * 100);
			// System.out.println();
		} catch (IndexOutOfBoundsException e) {
			System.out
					.println("SimulationLog: getTeamProfits problem line 267");
		}
		return profits;

	}

	/**
	 * @return The simulation's map of CIs and their affected services
	 *         (key=ci_id, value=set of affected services)
	 */
	HashMap<Byte, HashSet<Byte>> getAffectingCis() {
		return affecting_cis;
	}

	/**
	 * @return The simulation's map of services and their affecting CIs
	 *         (key=service_id, value=set of affecting CIs)
	 */
	HashMap<Byte, HashSet<Byte>> getAffectedServices() {
		return affected_services;
	}

	/**
	 * @param ci_id
	 *            The CI id
	 * @return The CI's solution cost
	 */
	double getCISolutionCost(byte ci_id) {
		return ciSolCosts.get(ci_id);
	}

	/**
	 * Calculates the derived cost of a service by dividing the CI solution cost
	 * to the amount of its affected services.
	 * 
	 * @param ci_id
	 *            The CI ID
	 * @return The service solution cost.
	 */
	double getServiceSolutionCost(byte ci_id) {
		return getCISolutionCost(ci_id) / getAffectingCis().get(ci_id).size();
	}

	/**
	 * Fires an incident in the teams' logs
	 * 
	 * @param ci_id
	 *            The incident that has been fired
	 * @param time
	 *            The time of the incident
	 */
	public void incidentsStarted(HashSet<Byte> ci_ids, SimulationTime time) {
		marom.incidentsStarted(ci_ids, time);
		rakia.incidentsStarted(ci_ids, time);
	}

	/**
	 * Updates the the teams' profits
	 * 
	 * @param time
	 *            The time of update
	 */
	public void updateTeamProfits(SimulationTime time) {
		marom.updateProfit(time, 500);
		rakia.updateProfit(time, 500);
	}

	/**
	 * Stops the log. Puts end times to both teams' services.
	 * 
	 */
	public void stopLogs(SimulationTime endTime) {
		marom.stop(endTime);
		rakia.stop(endTime);
	}

	/**
	 * Checks if the given incident in the given time is open
	 * 
	 * @param team
	 *            The team that the check refers to
	 * @param ci_id
	 *            The incident to be checked
	 * @param time
	 *            The time to be checked
	 * @return True if the incident is open. False otherwise.
	 */
	public boolean checkIncident(boolean team, byte ci_id, SimulationTime time) {
		return getTeam(team).isIncidentOpen(ci_id, time);
	}

	/**
	 * @return The incidents (key=start_time, value=HashSet of ci_ids)
	 */
	public HashMap<SimulationTime, HashSet<Byte>> getIncidents() {
		return time_cis;
	}

	public Set<SolutionLog> getSolutionHistory() {
		return solutionHistory;
	}

	/**
	 * @return The simulation's live queue of current solutions
	 */
	public LinkedList<SolutionLog> getSolutionQueue() {
		return solutionQueue;
	}

	/**
	 * Fixed all the incidents to both team
	 * 
	 * @param time
	 *            The current time
	 */
	public void fixAllIncidents(SimulationTime time) {
		marom.fixAllIncidents(time);
		rakia.fixAllIncidents(time);
	}

	public Settings getSettings() {
		return settings;
	}

	public int getRound() {
		return round;
	}

	public HashSet<SimulationTime> getCiTimes(byte ci_id) {
		return cis_time.get(ci_id);
	}

	/**
	 * @param settings
	 *            The course's settings
	 * @return The events considering stretching and pause times
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> getEventsForHomeScreen(int round) {
		List<JSONObject> eventList = new ArrayList<JSONObject>();

		if (time_events != null) {
			for (Map.Entry<SimulationTime, HashSet<String>> round_events : time_events
					.entrySet()) {
				JSONObject row = new JSONObject();
				row.put("time", round_events.getKey().getRunTime());
				row.put("session", round_events.getKey().getSessionInRound());
				JSONArray events = new JSONArray();
				events.addAll(round_events.getValue());
				row.put("events", events);
				eventList.add(row);
			}
		}
		return eventList;
	}

	public void updateSettings(Settings newSettings) {
		settings = newSettings;
	}

	public static Boolean getTeamConst(String teamName) {
		if (teamName.equalsIgnoreCase("Marom")) {
			return MAROM;
		} else if (teamName.equalsIgnoreCase("Rakia")) {
			return RAKIA;
		}
		return null;
	}

	public static boolean getServerPaused() {
		return serverPaused;
	}

	public static void setServerPaused(boolean isPaused) {
		serverPaused = isPaused;
	}

	public static boolean getClientPaused() {
		return clientPaused;
	}

	public static void setClientPaused(boolean isPaused) {
		clientPaused = isPaused;
	}

	public double getMTBF(boolean team) {
		Double mtbf = getTeam(team).getMTBF();
		if (mtbf == null) {
			// mtbf = round time
			return settings.getRoundTime();
		}
		return mtbf;
	}

	public double getMTRS(boolean team) {
		return getTeam(team).getMTRS();
	}

	public boolean checkSolution(String courseName, String team,
			int question_id, SimulationTime time, int solution, boolean isBought) {

		boolean isSolved = false;
		boolean temConst = getTeamConst(team);
		TeamLog teamLog = getTeam(temConst);
		if (teamLog == null) {
			// should not happen
			return false;
		}
		Byte ci_id = question_ci.get(question_id);
		if (ci_id == null) {
			// the question does not exist
			System.err.println(time.toString() + ": team = " + team + "question_id= " + question_id + ". ci_id= " + ci_id +". The question does not exist.\n");
			return false;
		}

		boolean is_open = teamLog.isIncidentOpen(ci_id, time);
		if (!is_open) {
			// ci is up
			System.err.println(time.toString() + ": team = " + team + "question_id= " + question_id + ". ci_id= " + ci_id +". The CI is already up.\n");
			return false;
		}

		if (isBought) {
			isSolved = true;
		} else {
			// check correctness of the solution
			SolutionElement sol = LogUtils.getCiSolutions().get(ci_id);
			if (sol == null) {
				// should not happen
				return false;
			}
			int real_solution = (temConst) ? sol.getSolution_marom() : sol
					.getSolution_rakia();
			isSolved = solution == real_solution;
		}

		if (!isSolved) {
			return false;
		}
		
		System.out.print(time.toString() + ": team = " + team + ". question_id= " + question_id + ". ci_id= " + ci_id +". Incident solved! ");
		HashSet<Byte> servicesFixed = teamLog.incidentSolved(ci_id, time, isBought);
		teamLog.increaseScore(servicesFixed.size());
		
		if (!servicesFixed.isEmpty()){
			System.out.println("Service fixed: " + servicesFixed);
			SolutionLog sol = new SolutionLog(courseName, team, servicesFixed);
			solutionQueue.offer(sol);
			solutionHistory.add(sol);
		}
		
		return true;
	}
}
