package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.SimulationTime;

import com.daoImpl.TblServiceDaoImpl;
import com.google.gson.JsonObject;
import com.model.TblService;

public class SimulationLog extends Thread implements Serializable {

	public static final boolean MAROM = true;
	public static final boolean RAKIA = false;

	private static final long serialVersionUID = 1L;
	/**
	 * The simulation's incidents and their times (key=start_time,
	 * value=incident_id)
	 */
	private HashMap<SimulationTime, Byte> incident_times = new HashMap<>();
	/**
	 * The simulation's incidents and their times (key=incident_id,
	 * value=start_time)
	 */
	private HashMap<Byte,SimulationTime> times_incidents;
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
	 * The simulation's incidents and their events (key = incident_id, value=set
	 * of events)
	 */
	private HashMap<Byte, HashSet<String>> incident_events;

	/**
	 * The simulation's CI price list (key=ci_id, value=solution cost)
	 */
	private HashMap<Byte, Double> ciSolCosts;
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

		settings = _settings;
		affecting_cis = LogUtils.getDBAffectingCIs();
		affected_services = LogUtils.getDBAffectedServices();
		ciSolCosts = LogUtils.getCISolCosts();
		incident_times = LogUtils.getIncidentTimes();
		times_incidents = LogUtils.getTimeIncidents();
		incident_events = LogUtils.getIncidentEvents();

		List<TblService> services = new TblServiceDaoImpl().getAllServices();
		HashMap<Byte, Double> serviceDownTimeCosts = LogUtils
				.getServiceDownTimeCosts();
		HashMap<Byte, ServiceLog> service_logs = new HashMap<>();

		double initDiff = 0;

		// initializes service logs
		if (services != null) {
			for (TblService service : services) {
				byte service_id = service.getServiceId();
				service_logs.put(
						service_id,
						new ServiceLog(service_id, service.getFixedCost(),
								service.getFixedIncome(), serviceDownTimeCosts
										.get(service_id)));
				initDiff += service_logs.get(service_id).getDiff();
			}
		}

		@SuppressWarnings("unchecked")
		HashMap<Byte, ServiceLog> service_logs_copy = (HashMap<Byte, ServiceLog>) LogUtils
				.copy(service_logs);

		marom = new TeamLog("Marom", service_logs, initDiff);
		rakia = new TeamLog("Rakia", service_logs_copy, initDiff);

		System.out.println("SimulationLog: SimulationLog is initalized.");
	}

	/**
	 * Sets he round of the service log, updates the round of the team logs.
	 * 
	 * @param currentRound
	 *            The current round.
	 */
	public void setRound(int currentRound) {
		round = currentRound;
		marom.setRound(currentRound);
		rakia.setRound(currentRound);

		// initializes incident logs
		HashMap<Byte, IncidentLog> incident_logs_current_round = new HashMap<>();
		Collection<IncidentLog> inc_logs_all_rounds = (Collection<IncidentLog>) LogUtils
				.getIncidentLogs().values();
		if (inc_logs_all_rounds != null) {
			for (IncidentLog inc_log : inc_logs_all_rounds) {
				int inc_round = inc_log.getStart_time().getRound();
				if (inc_round == round) {
					incident_logs_current_round.put(inc_log.getIncident_id(),
							inc_log);
				}
			}
		}

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
			System.out.println("SimulationLog: getTeamProfits problem line 202");
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
	 * @return The events of the incident
	 */
	HashSet<String> getIncidentEvents(byte inc_id) {
		return incident_events.get(inc_id);
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
	 * Fires a solution in the team's logs
	 * 
	 * @param team
	 *            The team that solved the incident
	 * @param inc_id
	 *            The incident that was solved
	 * @param time
	 *            The time when the incident was solved
	 * @param isBought
	 *            True weather the incident was bought. False otherwise.
	 */
	public boolean incidentSolved(boolean team, byte inc_id,
			SimulationTime time, boolean isBought) {
		return getTeam(team).incidentSolved(inc_id, time, isBought);
	}

	/**
	 * Fires an incident in the teams' logs
	 * 
	 * @param inc_id
	 *            The incident that has been fired
	 * @param time
	 *            The time of the incident
	 */
	public void incidentStarted(byte inc_id, SimulationTime time) {
		marom.incidentStarted(inc_id, time);
		rakia.incidentStarted(inc_id, time);
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
	public void stopLogs() {
		marom.stop();
		rakia.stop();
	}

	/**
	 * Checks if the given incident in the given time is open
	 * 
	 * @param team
	 *            The team that the check refers to
	 * @param inc_id
	 *            The incident to be checked
	 * @param time
	 *            The time to be checked
	 * @return True if the incident is open. False otherwise.
	 */
	public boolean checkIncident(boolean team, byte inc_id, SimulationTime time) {
		return getTeam(team).isIncidentOpen(inc_id, time);
	}

	/**
	 * @return The simulation's incidents and their times (key=start_time,
	 *         value=incident_id)
	 */
	public HashMap<SimulationTime, Byte> getIncidentTimes() {
		return incident_times;
	}

	public void addSolution(SolutionLog sol) {
		solutionQueue.offer(sol);
		solutionHistory.add(sol);
	}
	
	public Set<SolutionLog> getSolutionHistory(){
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
	
	public SimulationTime getTimeOfIncident(byte inc_id){
		return times_incidents.get(inc_id);
	}

	/**
	 * @param settings
	 *            The course's settings
	 * @return The events considering stretching and pause times
	 */
	public List<JsonObject> getEventsForHomeScreen() {
		List<JsonObject> eventList = new ArrayList<JsonObject>();

		if (incident_times != null) {
			
			for (Map.Entry<SimulationTime, Byte> incident : incident_times
					.entrySet()) {
				HashSet<String> events = incident_events.get(incident
						.getValue());

				if (events == null) {
					continue;
				}

				for (String event : events) {
					JsonObject row = new JsonObject();
					row.addProperty("time", incident.getKey().getRunTime());
					row.addProperty("round", incident.getKey().getRound());
					row.addProperty("session", incident.getKey().getSessionInRound());
					row.addProperty("event_id", Integer.valueOf(event));
					eventList.add(row);
				}
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
}
