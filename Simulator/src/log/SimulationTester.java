package log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SimulationTester implements Runnable {
	/**
	 * The simulation log - for accessing a team
	 */
	private SimulationLog simLog;
	/**
	 * The simulation's elapsed time
	 */
	private int elapsed_time;
	/**
	 * A team for the simulation test
	 */
	private TeamLog marom;
	/**
	 * The course's settings
	 */
	private Settings settings;
	/**
	 * A map of services and their SLA times - key=service , value= max time to
	 * fix
	 */
	private HashMap<Integer, Integer> services_sla;
	/**
	 * A schedule of future solutions - key=service, value=time to solve
	 */
	public HashMap<Integer, Integer> solutions_schedule;
	
	private int percentage = 0; 
	
	public SimulationTester(Settings settings) {
		super();
		this.settings = settings;
		LogUtils.saveSettings(settings);
		simLog = SimulationLog.getInstance(settings.getCourseName());
		marom = simLog.getTeam("marom");
		
		HashMap<Integer, String> servicePriorities = LogUtils.getServicePriorities();
		HashMap<String, Integer> priority_sla = settings.getPriority_sla();

		solutions_schedule = new HashMap<>();
		services_sla = new HashMap<>();

		for (Map.Entry<Integer, String> sp : servicePriorities.entrySet()) {
			services_sla.put(sp.getKey(), priority_sla.get(sp.getValue()));
		}
	}

	@Override
	public void run() {
		// TODO: fix loop condition
		while (++elapsed_time <= settings.getTotalRunTime()) {
//			int newPercentage = (int) ((double)elapsed_time/settings.getTotalRunTime()*100);
//			if (newPercentage > percentage){
//				percentage = newPercentage;
//				System.out.println("SimutaltionTester: " + percentage + "%");
//			}
			
			if (simLog.getIncidentTimes().containsKey(elapsed_time)) {

				int inc_id = simLog.getIncidentTimes().get(elapsed_time);
				int ci_id = marom.getIncident_logs().get(inc_id).getRoot_ci();

				marom.incidentStarted(inc_id, elapsed_time);
				updateSLASolutions(ci_id, elapsed_time);
			}

			/*
			 * solves all the CIs related to a service that should be solved now
			 * according its SLA, and updates the team's profit accordingly.
			 */
			@SuppressWarnings("unchecked")
			HashMap<Integer, Integer> sla_solutions_copy = (HashMap<Integer, Integer>) LogUtils
					.copy(solutions_schedule);
			for (Map.Entry<Integer, Integer> sla_sol : sla_solutions_copy.entrySet()) {
				if (sla_sol.getValue().intValue() == elapsed_time) {
					solve(sla_sol.getKey());
				}
			}
			marom.updateProfit(elapsed_time,0);
		}

		/*
		 * Saving the target scores for each round as benchmark
		 */
		int rounds = settings.getRounds();
		int roundRunTime = settings.getRunTime() * settings.getSessionsPerRound();
		
		System.out.print("Simulation tester finished: Target scores are: ");
		ArrayList<Integer> targetScores = new ArrayList<>(rounds);
		for (int r = 1; r <= rounds; r++) {
			int targetScore = marom.getProfits().get(roundRunTime * r).intValue();
			System.out.print(targetScore + ((r<rounds)? ", " : ""));
			targetScores.add(targetScore);
		}
		System.out.println("");
		settings.setTargetScores(targetScores);
		LogUtils.saveSettings(settings);
	}

	/**
	 * Solves all CIs related to a certain service and updates the future SLA
	 * solutions
	 * 
	 * @param service_id
	 */
	private void solve(int service_id) {
		HashSet<Integer> cis = simLog.getAffectedServices().get(service_id);
		for (Integer ci : cis) {
			simLog.incidentSolved("Marom", ci, service_id, false);
			updateSLASolutions(ci, service_id);
		}
	}

	/**
	 * Maintains a map of services and the optimal time to fix them according to
	 * the SLA
	 * 
	 * @param elapsed_time
	 */
	private void updateSLASolutions(int ci_id, int elapsed_time) {

		HashSet<Integer> affectedServices = simLog.getAffectingCis().get(ci_id);

		if (affectedServices == null) {
			return;
		}

		for (Integer affService : affectedServices) {
			if (marom.getService_logs().get(affService).isUp()) {
				// the service is already up - no need to fix it
				solutions_schedule.remove(affService);
			} else {
				// the service is down - a fix is scheduled
				solutions_schedule.put(affService, services_sla.get(affService) + elapsed_time);
			}
		}
	}
}
