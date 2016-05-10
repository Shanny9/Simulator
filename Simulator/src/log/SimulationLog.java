package log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class SimulationLog extends Thread implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * key=start_time, value=incident_id
	 */
	private HashMap<Integer, Integer> incident_times;
	/*
	 * key=ci_id, value=set of affected services
	 */
	private HashMap<Integer, HashSet<Integer>> affecting_cis;
	/*
	 * key=service_id, value=set of affecting CIs
	 */
	private HashMap<Integer, HashSet<Integer>> affected_services;
	/*
	 * key=ci_id, value=solution cost
	 */
	private HashMap<Integer, Double> ciSolCosts;
	/*
	 * The log of team Marom
	 */
	private static TeamLog marom;
	/*
	 * The log of team Rakia
	 */
	private static TeamLog rakia;
	/*
	 * An updating queue of current solutions
	 */
	private LinkedList<SolutionLog> solutionQueue;
	
	private static SimulationLog instance;
	/**
	 * @param cis
	 * @param servicesItems
	 */
	SimulationLog() {
		super();
		affecting_cis = LogUtils.getDBAffectingCIs();
		affected_services = LogUtils.getDBAffectedServices();
		ciSolCosts = LogUtils.getCISolCosts();
		incident_times = LogUtils.getIncidentTimes();
		solutionQueue = new LinkedList<>();
		
		marom = new TeamLog();
		rakia = new TeamLog();
	}

	public static SimulationLog getInstance() {
		if (instance == null) {
			System.out.println("Log is created");
			instance = new SimulationLog();
		}
		return instance;
	}

	public TeamLog getTeam(String team) {
		if (team.equals("Marom")) {
			return marom;
		} else if (team.equals("Rakia")) {
			return rakia;
		}
		return null;
	}

	HashMap<Integer, HashSet<Integer>> getAffectingCis() {
		return affecting_cis;
	}

	HashMap<Integer, HashSet<Integer>> getAffectedServices() {
		return affected_services;
	}

	double getCISolutionCost(int ci_id){
		return ciSolCosts.get(ci_id);
	}
	
	public void incidentSolved(String team, int inc_id, int time, boolean isBaught) {
		getTeam(team).incidentSolved(inc_id, time, isBaught);
	}

	public boolean checkIncident(String team, int inc_id, int time) {
		return getTeam(team).isIncidentOpen(inc_id, time);
	}
	
	public HashMap<Integer, Integer> getIncidentTimes(){
		return incident_times;
	}
	
	public LinkedList<SolutionLog> getSolutionQueue(){
		return solutionQueue;
	}

	public void fixAllIncidents(int time) {
		marom.fixAllIncidents(time);
		rakia.fixAllIncidents(time);
	}

}
