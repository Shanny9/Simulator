package log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.daoImpl.TblGeneralParametersDaoImpl;
import com.daoImpl.TblServiceDaoImpl;
import com.model.TblService;

public class SimulationLog extends Thread implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The simulation's incidents and their times (key=start_time, value=incident_id)
	 */
	private HashMap<Integer, Integer> incident_times;
	/**
	 * The simulation's map of CIs and their affected services (key=ci_id, value=set of affected services)
	 */
	private HashMap<Integer, HashSet<Integer>> affecting_cis;
	/**
	 * The simulation's map of services and their affecting CIs (key=service_id, value=set of affecting CIs)
	 */
	private HashMap<Integer, HashSet<Integer>> affected_services;
	/**
	 * The simulation's incidents and their events
	 */
	private HashMap<Integer, HashSet<String>> incident_events;
	/**
	 * The simulation's CI price list (key=ci_id, value=solution cost)
	 */
	private HashMap<Integer, Double> ciSolCosts;
	/**
	 * The simulation's live queue of current solutions
	 */
	private LinkedList<SolutionLog> solutionQueue;
	/**
	 * The log of team Marom
	 */
	private static TeamLog marom;	
	/**
	 * The log of team Rakia
	 */
	private static TeamLog rakia;
	/**
	 * The Simulation log's instance
	 */
	private static SimulationLog instance;


	SimulationLog() {
		super();
		affecting_cis = LogUtils.getDBAffectingCIs();
		affected_services = LogUtils.getDBAffectedServices();
		ciSolCosts = LogUtils.getCISolCosts();
		incident_times = LogUtils.getIncidentTimes();
		incident_events = LogUtils.getIncidentEvents();
		solutionQueue = new LinkedList<>();
		double initProfit = new TblGeneralParametersDaoImpl().getGeneralParameters().getInitialCapital();

		List<TblService> services = new TblServiceDaoImpl().getAllServices();
		HashMap<Integer, Double> serviceDownTimeCosts = LogUtils.getServiceDownTimeCosts();
		HashMap<Integer, ServiceLog> service_logs = new HashMap<>();
		
		double initDiff = 0;
		
		for (TblService service : services) {
			int service_id = service.getServiceId();
			service_logs.put(service_id, new ServiceLog(service_id, service.getFixedCost(), service.getFixedIncome(),
					serviceDownTimeCosts.get(service_id)));
			initDiff += service_logs.get(service_id).getDiff();
		}
		
		HashMap<Integer, IncidentLog> incident_logs = LogUtils.getIncidentLogs();
		
		marom = new TeamLog(initProfit, service_logs, initDiff, incident_logs);
		rakia = new TeamLog(initProfit, service_logs, initDiff, incident_logs);
	}

	public static SimulationLog getInstance() {
		if (instance == null) {
			System.out.println("Log is created");
			instance = new SimulationLog();
		}
		return instance;
	}

	/**
	 * @param team The team's name
	 * @return The team's log
	 */
	public TeamLog getTeam(String team) {
		if (team.equalsIgnoreCase("Marom")) {
			return marom;
		} else if (team.equalsIgnoreCase("Rakia")) {
			return rakia;
		}
		return null;
	}

	/**
	 * @return The simulation's map of CIs and their affected services (key=ci_id, value=set of affected services)
	 */
	HashMap<Integer, HashSet<Integer>> getAffectingCis() {
		return affecting_cis;
	}

	/**
	 * @return The simulation's map of services and their affecting CIs (key=service_id, value=set of affecting CIs)
	 */
	HashMap<Integer, HashSet<Integer>> getAffectedServices() {
		return affected_services;
	}
	
	/**
	 * @return The events of the incident
	 */
	HashSet<String> getIncidentEvents(int inc_id) {
		return incident_events.get(inc_id);
	}

	/**
	 * @param ci_id The CI id
	 * @return The CI's solution cost
	 */
	double getCISolutionCost(int ci_id){
		return ciSolCosts.get(ci_id);
	}
	
	/**
	 * Updates the team's diff, purchases and profits given the incident solved
	 * 
	 * @param team The team that solved the incident
	 * @param inc_id The incident that was solved
	 * @param time The time when the incident was solved
	 * @param isBought True weather the incident was bought. False otherwise.
	 */
	public void incidentSolved(String team, int inc_id, int time, boolean isBought) {
		getTeam(team).incidentSolved(inc_id, time, isBought);
	}

	/**
	 * Checks if the given incident in the given time is open
	 * 
	 * @param team The team that the check refers to
	 * @param inc_id The incident to be checked
	 * @param time The time to be checked
	 * @return True if the incident is open. False otherwise.
	 */
	public boolean checkIncident(String team, int inc_id, int time) {
		return getTeam(team).isIncidentOpen(inc_id, time);
	}
	
	/**
	 * @return The simulation's incidents and their times (key=start_time, value=incident_id)
	 */
	public HashMap<Integer, Integer> getIncidentTimes(){
		return incident_times;
	}
	
	/**
	 * @return The simulation's live queue of current solutions
	 */
	public LinkedList<SolutionLog> getSolutionQueue(){
		return solutionQueue;
	}

	/**
	 * Fixed all the incidents to both team
	 * @param time The current time
	 */
	public void fixAllIncidents(int time) {
		marom.fixAllIncidents(time);
		rakia.fixAllIncidents(time);
	}
}
