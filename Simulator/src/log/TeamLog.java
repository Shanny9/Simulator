package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import utils.SimulationTime;

/**
 * TeamLog records and calculates the team's services, purchases and profits
 * throughout the simulation
 */
public class TeamLog implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The team's name
	 */
	private String teamName;
	/**
	 * The round of the team log
	 */
	private int round;
	/**
	 * The team's incident logs (key=ci_id, value=log)
	 */
	private HashMap<Byte, IncidentLog> incident_logs;
	/**
	 * The team's service logs
	 */
	private HashMap<Byte, ServiceLog> service_logs;
	/**
	 * The team's list of purchases: key=time, value=ci_id
	 */
	private HashMap<Integer, Byte> purchases;
	/**
	 * The team's history of profits;
	 */
	private ArrayList<Double> profits;
	/**
	 * The team's history of profits;
	 */
	private double diff;
	/**
	 * The status of the log
	 */
	private boolean isFinished;
	/**
	 * The team's score
	 */
	private int score = 0;

	/**
	 * @param initProfit
	 *            The team's initial profit
	 * @param service_logs
	 *            the team's initial service logs
	 * @param initDiff
	 *            the team's initial money gain per second
	 * @param incident_logs
	 *            the team's initial incident logs
	 */
	TeamLog(String teamName, HashMap<Byte, ServiceLog> service_logs,
			double initDiff) {

		super();
		this.teamName = teamName;
		this.purchases = new HashMap<>();
		this.service_logs = new HashMap<>();
		this.incident_logs = new HashMap<>();
		this.isFinished = false;
		this.service_logs.putAll(service_logs);
		this.diff = initDiff;
	}

	/**
	 * Sets the round of the team log and the initial profit of the round.
	 * 
	 * @param round
	 *            Round to set.
	 */
	void setRound(int round) {
		this.round = round;
		Settings sett = SimulationLog.getInstance().getSettings();
		int duration = sett.getRoundRunTime();
		double init_capital = 0;

		if (round == 1) {
			init_capital = sett.getInitCapital();
		} else {
			SimulationTime roundTime = new SimulationTime(sett.getRunTime());
			SimulationLog simLog = FilesUtils.openLog(sett.getCourseName(),
					round - 1);
			if (simLog != null) {
				init_capital = simLog.getTeam(
						SimulationLog.getTeamConst(teamName)).getProfit(
						roundTime);
			}
		}

		this.profits = new ArrayList<>(Collections.nCopies(duration + 1, 0d));
		setProfit(new SimulationTime(0), init_capital);

		HashSet<Byte> cis = LogUtils.getCisInRound(round);
		for (Byte ci_id : cis) {
			incident_logs.put(ci_id, new IncidentLog(ci_id));
		}
	}

	/**
	 * @return the name of the team.
	 */
	String getTeamName() {
		return teamName;
	}

	/**
	 * Updates the team's diff, purchases and profits given the incident solved
	 * 
	 * @param ci_id
	 *            the incident that was solved
	 * @param time
	 *            the time when the incident was solved
	 * @param isBought
	 *            indicates if the incident was bought or not.
	 */
	synchronized HashSet<Byte> incidentSolved(byte ci_id, SimulationTime time,
			boolean isBought) {

		if (isFinished) {
			return null;
		}

		incident_logs.get(ci_id).close(time);

		HashSet<Byte> affectedServices = SimulationLog.getInstance()
				.getAffectingCis().get(ci_id);

		HashSet<Byte> affectedServicesDown = new HashSet<>();

		if (affectedServices != null) {
			System.out.println(time.toString() + ": team = " + teamName
					+ "ci_id= " + ci_id + ".\nAffected services: "
					+ affectedServices + "\n");
			for (Byte service_id : affectedServices) {
				ServiceLog sl = service_logs.get(service_id);
				if (!sl.isUp()) {
					affectedServicesDown.add(service_id);
				}
				diff += sl.ciUpdate(ci_id, true, time);
			}
		}

		if (isBought) {
			purchases.put(time.getRunTime(), ci_id);
			double solutionCost = SimulationLog.getInstance()
					.getCISolutionCost(ci_id);
			double profitToSet = getProfit(time) - solutionCost;
			setProfit(time, profitToSet);
			// System.out.println("Team " + teamName + ": solution baught at " +
			// time.toString() + ".");
		} else {
			// System.out.println("Team " + teamName + ": solution solved at " +
			// time.toString() + ".");
		}

		HashSet<Byte> servicesFixed = new HashSet<>();
		if (affectedServices != null) {
			for (Byte service_id : affectedServices) {
				ServiceLog sl = service_logs.get(service_id);
				if (sl.isUp() && affectedServicesDown.contains(service_id)) {
					servicesFixed.add(service_id);
				}
			}
		}
		return servicesFixed;
	}

	/**
	 * Solves all the CI's affected services.
	 * 
	 * @param ci_id
	 *            the ID of the CI that was solved
	 * @param timethe
	 *            time of solution
	 */
	void ciSolved(byte ci_id, SimulationTime time) {

		HashSet<Byte> affectedServices = SimulationLog.getInstance()
				.getAffectingCis().get(ci_id);

		if (affectedServices != null) {
			for (Byte service_id : affectedServices) {
				diff += service_logs.get(service_id)
						.ciUpdate(ci_id, true, time);
			}
		}
	}

	/**
	 * Updates the team's diff given the incident id and time.
	 * 
	 * @param ci_id
	 *            The incident that was started
	 * @param time
	 *            The time when the incident started
	 */
	synchronized void incidentsStarted(HashSet<Byte> ci_ids, SimulationTime time) {
		if (isFinished) {
			return;
		}

		HashSet<Byte> affectedServices = new HashSet<>();
		for (Byte ci_id : ci_ids) {
			if (ci_id != null) {
				incident_logs.get(ci_id).open(time);
				affectedServices.addAll(SimulationLog.getInstance()
						.getAffectingCis().get(ci_id));
			}
		}

		if (affectedServices != null) {
			for (Byte service_id : affectedServices) {
				for (Byte ci_id : ci_ids) {
					if (ci_ids != null) {
						diff += service_logs.get(service_id).ciUpdate(ci_id,
								false, time);
					}
				}
			}
		}
	}

	/**
	 * @param time
	 *            The simulation time
	 * @return The team's profit at the given time
	 */
	public synchronized double getProfit(SimulationTime time) {
		if (profits != null) {
			return profits.get(time.getRunTimeInRound());
		} else {
			return 0;
		}
	}

	private synchronized void setProfit(SimulationTime time, double profitToSet) {
		profits.set(time.getRunTimeInRound(), profitToSet);
	}

	/**
	 * @return The team's current profit
	 */
	/*
	 * public double getCurrentProfit() { return profits.get(profits.size() -
	 * 1); }
	 */

	/**
	 * @param time
	 *            updates the team's profit according to the diff
	 */
	synchronized void updateProfit(SimulationTime time, int delayInMilis) {
		if (delayInMilis > 0) {
			try {
				wait(delayInMilis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (isFinished) {
			return;
		}
		double profitToSet = getProfit(new SimulationTime(
				time.getRunTimeInRound() - 1))
				+ diff;
		setProfit(time, profitToSet);
	}

	/**
	 * @param service_id
	 * @return The team's service_logs
	 */
	public ServiceLog getService_log(byte service_id) {
		return service_logs.get(service_id);
	}

	/**
	 * @return The team's purchases (key= time, value=ci_id)
	 */
	HashMap<Integer, Byte> getPurchaces() {
		return purchases;
	}

	/**
	 * Stops the team's log
	 * 
	 * @param time
	 *            Current time
	 */
	public void stop(SimulationTime endTime) {
		// sets the end time of the round in all the service logs' ArrayList of
		// times.

		if (service_logs != null) {
			for (ServiceLog service : service_logs.values()) {
				service.stop(endTime);
			}
		}

		// closes all unclosed incidents
		if (incident_logs != null) {
			for (IncidentLog inc_log : incident_logs.values()) {
				inc_log.close(endTime);
			}
		}

		// iterates the team's purchase list, retrieves the cost of each CI
		// purchase, calculates the cost of each of the CI's affected services
		// and updates the services' purchase costs accordingly.
		if (purchases != null) {
			for (Byte ci_id : purchases.values()) {
				double ci_solution_cost = SimulationLog.getInstance()
						.getCISolutionCost(ci_id);
				HashSet<Byte> affected_services = SimulationLog.getInstance()
						.getAffectingCis().get(ci_id);
				double service_solution_cost = ci_solution_cost
						/ affected_services.size();
				for (Byte service_id : affected_services) {
					service_logs.get(service_id)
							.purchase(service_solution_cost);
				}
			}
		}
		this.isFinished = true;
	}

	/**
	 * Checks if the given incident in the given time is open
	 * 
	 * @param ci_id
	 *            The incident to check
	 * @param time
	 *            The time to check
	 * @return True if the incident is open. False otherwise.
	 */
	boolean isIncidentOpen(byte ci_id, SimulationTime simTime) {

		if (ci_id <= 0) {
			return false;
		}

		IncidentLog il = incident_logs.get(ci_id);
		if (il == null) {
			return false;
		}

		// System.out.println(
		// "TeamLog isIncidentOpen: time= " + time + ", isOpen= " +
		// incident_logs.get(inc_id).isOpen(time));
		return incident_logs.get(ci_id).isOpen(simTime);

	}

	/**
	 * Fixes all the team's open incidents
	 * 
	 * @param time
	 *            Current time
	 */
	public void fixAllIncidents(SimulationTime time) {
		if (incident_logs != null) {
			for (byte ci_id : incident_logs.keySet()) {
				// TODO: change false back to true
				incidentSolved(ci_id, time, false);
			}
		}
	}

	/**
	 * @return The team's profits (index= time)
	 */
	public ArrayList<Double> getProfits() {
		return this.profits;
	}

	public int getScore() {
		return this.score;
	}

	void increaseScore(int num) {
		this.score += num;
	}

	public HashMap<Byte, IncidentLog> getClosedIncidentLogs() {
		HashMap<Byte, IncidentLog> result = new HashMap<>();
		if (incident_logs != null) {
			for (IncidentLog incLog : incident_logs.values()) {
				if (!incLog.isOpen()) {
					result.put(incLog.getCiId(), incLog);
				}
			}
		}
		return result;
	}

	/**
	 * @return The team's Mean Time to Restore Service (MTRS) In case of no
	 *         failures, returns 0
	 */
	double getMTRS() {
		int trs = 0;

		if (service_logs != null) {
			for (ServiceLog sl : service_logs.values()) {
				trs += sl.getMTRS();
			}

			if (service_logs.size() > 0) {
				return (double) trs / service_logs.size();
			}
		}
		return 0;
	}

	/**
	 * @return The team's Mean Between Failures (MTBF) In case of no failures,
	 *         returns -1
	 */
	Double getMTBF() {
		int tbf = 0;
		if (service_logs != null) {
			for (ServiceLog sl : service_logs.values()) {
				tbf += sl.getMTBF();
			}

			if (service_logs.size() > 0) {
				return (double) tbf / service_logs.size();
			}
		}
		return 0.d;
	}

	/**
	 * @return the mean time of the team's availability percentage
	 */
	public double getUpTimePercentage() {
		int sum = 0;

		if (service_logs != null) {
			for (ServiceLog sl : service_logs.values()) {
				sum += sl.getUpTimePercentage();
			}
		}
		return (double) sum / service_logs.size();
	}

	public String toString() {
		String str = "Team " + teamName + "\n========\n\nIncidents\n--------\n";
		if (incident_logs != null) {
			for (IncidentLog il : incident_logs.values()) {
				str += il.toString() + "\n";
			}
		}
		str += "\nServices\n--------\n";
		if (service_logs != null) {
			for (ServiceLog sl : service_logs.values()) {
				str += sl.toString() + "\n";
			}
		}
		str += "\nPurchases\n--------\n";
		if (purchases != null) {
			for (Entry<Integer, Byte> entry : purchases.entrySet()) {
				str += "Time= " + entry.getKey() + ", CI ID= "
						+ entry.getValue() + "\n";
			}
		}
		return str;
	}
}
