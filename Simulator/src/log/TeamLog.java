package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * TeamLog records and calculates the team's services, purchases and profits
 * throughout the simulation
 */
public class TeamLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The course's name
	 */
	private String courseName;
	/**
	 * The team's name
	 */
	private String teamName;
	/**
	 * The team's incident logs
	 */
	private HashMap<Integer, IncidentLog> incident_logs;
	/**
	 * The team's service logs
	 */
	private HashMap<Integer, ServiceLog> service_logs;
	/**
	 * The team's list of purchases: key=time, value=ci_id
	 */
	private HashMap<Integer, Integer> purchases;
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
	 * @param initProfit
	 *            The team's initial profit
	 * @param service_logs
	 *            the team's initial service logs
	 * @param initDiff
	 *            the team's initial money gain per second
	 * @param incident_logs
	 *            the team's initial incident logs
	 */
	TeamLog(String courseName, String teamName, double initProfit, HashMap<Integer, ServiceLog> service_logs,
			double initDiff, HashMap<Integer, IncidentLog> incident_logs, int duration) {

		super();
		this.courseName = courseName;
		this.teamName = teamName;
		this.purchases = new HashMap<>();
		this.service_logs = new HashMap<>();
		this.incident_logs = new HashMap<>();
		this.isFinished = false;
		this.service_logs.putAll(service_logs);
		this.diff = initDiff;
		this.incident_logs.putAll(incident_logs);
		this.profits = new ArrayList<>(Collections.nCopies(duration + 1, 0d));
		this.profits.set(0, initProfit);
	}

	String getTeamName() {
		return teamName;
	}

	double getDiff() {
		return diff;
	}

	/**
	 * Updates the team's diff, purchases and profits given the incident solved
	 * 
	 * @param inc_id
	 *            The incident that was solved
	 * @param time
	 *            The time when the incident was solved
	 * @param isBaught
	 *            True if the incident was solved, otherwise false.
	 */
	synchronized boolean incidentSolved(int inc_id, int time, boolean isBaught) {

		if (isFinished || !isIncidentOpen(inc_id, time)) {
			return false;
		}

		IncidentLog incLog = incident_logs.get(inc_id);
		incLog.setEnd_time(time);
		int ci_id = incLog.getRoot_ci();

		HashSet<Integer> affectedServices = SimulationLog.getInstance().getAffectingCis().get(ci_id);

		if (affectedServices != null) {
			for (Integer service_id : affectedServices) {
				diff += service_logs.get(service_id).ciUpdate(ci_id, true, time);
			}
		}

		if (isBaught) {
			purchases.put(time, ci_id);
			profits.set(time, getProfit(time) - SimulationLog.getInstance().getCISolutionCost(ci_id));
			// System.out.println("Team " + teamName + ": solution baught at " +
			// time + "seconds for "
			// +
			// SimulationLog.getInstance(courseName).getCISolutionCost(ci_id));
		}
		return true;
	}

	void ciSolved(int ci_id, int time) {
		HashSet<Integer> affectedServices = SimulationLog.getInstance().getAffectingCis().get(ci_id);

		if (affectedServices != null) {
			for (Integer service_id : affectedServices) {
				diff += service_logs.get(service_id).ciUpdate(ci_id, true, time);
			}
		}
	}

	/**
	 * Updates the team's diff given the incident id and time.
	 * 
	 * @param inc_id
	 *            The incident that was started
	 * @param time
	 *            The time when the incident started
	 */
	synchronized void incidentStarted(int inc_id, int time) {
		if (isFinished) {
			return;
		}

		int ci_id = incident_logs.get(inc_id).getRoot_ci();
		HashSet<Integer> affectedServices = SimulationLog.getInstance().getAffectingCis().get(ci_id);
		if (affectedServices != null) {
			for (Integer service_id : affectedServices) {
				diff += service_logs.get(service_id).ciUpdate(ci_id, false, time);
			}
		}
	}

	/**
	 * @param time
	 *            The simulation time
	 * @return The team's profit at the given time
	 */
	synchronized public double getProfit(int time) {
		try {
			return profits.get(time);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return 0;
		}
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
	synchronized void updateProfit(int time, int delayInMilis) {
		if (delayInMilis > 0) {
			try {
				wait(delayInMilis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (isFinished) {
			return;
		}
		profits.set(time, getProfit(time - 1) + diff);
	}

	/**
	 * @return The team's service_logs
	 */
	HashMap<Integer, ServiceLog> getService_logs() {
		return service_logs;
	}

	/**
	 * @return The team's purchases (key= time, value=ci_id)
	 */
	HashMap<Integer, Integer> getPurchaces() {
		return purchases;
	}

	/**
	 * Stops the team's log
	 * 
	 * @param time
	 *            Current time
	 */
	public void stop(int time) {
		for (ServiceLog service : service_logs.values()) {
			service.stop(time);
		}
		this.isFinished = true;
	}

	/**
	 * Checks if the given incident in the given time is open
	 * 
	 * @param inc_id
	 *            The incident to check
	 * @param time
	 *            The time to check
	 * @return True if the incident is open. False otherwise.
	 */
	boolean isIncidentOpen(int inc_id, int time) {
		if (time < 0 || inc_id <= 0) {
			return false;
		}

		IncidentLog il = incident_logs.get(inc_id);
		if (il == null) {
			return false;
		}

		// System.out.println(
		// "TeamLog isIncidentOpen: time= " + time + ", isOpen= " +
		// incident_logs.get(inc_id).isOpen(time));
		return incident_logs.get(inc_id).isOpen(time);

	}

	/**
	 * Fixes all the team's open incidents
	 * 
	 * @param time
	 *            Current time
	 */
	public void fixAllIncidents(int time) {
		for (int inc_id : incident_logs.keySet()) {
			if (incident_logs.get(inc_id).isOpen(time)) {
				// TODO: change false back to true
				incidentSolved(inc_id, time, false);
			}
		}
	}

	/**
	 * @return The team's profits (index= time)
	 */
	public ArrayList<Double> getProfits() {
		return profits;
	}

	public HashMap<Integer, IncidentLog> getIncident_logs() {
		return incident_logs;
	}

	/**
	 * @return The team's Mean Time to Restore Service (MTRS)
	 */
	public double getMTRS() {
		int trs = 0;
		int failures = 0;

		for (ServiceLog sl : service_logs.values()) {
			trs += sl.getTRS();
			failures += sl.getNumOfFailures();
		}
		return (double) trs / failures;
	}

	public String toString() {
		String str = "Team " + teamName + "\n========\n\nIncidents\n--------\n";
		for (IncidentLog il : incident_logs.values()) {
			str += il.toString() + "\n";
		}
		str += "\nServices\n--------\n";
		for (ServiceLog sl : service_logs.values()) {
			str += sl.toString() + "\n";
		}
		str += "\nPurchases\n--------\n";
		for (Map.Entry<Integer, Integer> entry : purchases.entrySet()) {
			str += "Time= " + entry.getKey() + ", CI ID= " + entry.getValue() + "\n";
		}
		return str;
	}

}
