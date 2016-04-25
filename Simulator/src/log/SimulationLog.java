package log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class SimulationLog implements Runnable, Serializable {
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
	
	private static SimulationLog instance;
	/*
	 * False if simulator is running
	 */
	private static boolean stopThread;
	/*
	 * The run_time elapsed since the simulation started
	 */
	private int elapsed_time;
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

	public static void pause() {
		stopThread = true;
		log.LogUtils.saveLog();
	}

	public static void resume() {
		stopThread = false;
	}
	
	public static void Stop(int time){
		pause();
		marom.Stop(time);
		rakia.Stop(time);
		System.out.println("Log stopped");
	}

	@Override
	public void run() {
		while (!stopThread) {
			// should occur every second
			elapsed_time++;
			Integer inc_id  = incident_times.get(elapsed_time);
			if (inc_id != null){
				marom.incidentStarted(inc_id, elapsed_time);
				rakia.incidentStarted(inc_id, elapsed_time);
			}
			marom.updateProfit();
			rakia.updateProfit();
		}
	}

	public boolean checkIncident(String team, int inc_id, int time) {
		return getTeam(team).isIncidentOpen(inc_id, time);
	}

}
