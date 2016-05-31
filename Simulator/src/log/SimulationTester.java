package log;

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
	private HashMap<Integer, Integer> solutions_schedule;

	private HashMap<Integer, HashSet<Integer>> ci_incidents;

	private int roundRunTime;

	private int currentRound;

	private int nextRoundEnd;

	public SimulationTester(Settings settings) {
		super();
		LogUtils.saveSettings(settings);
		this.settings = settings;
		this.currentRound = 1;
		this.roundRunTime = settings.getRoundTime() * settings.getSessionsPerRound();
		this.nextRoundEnd = roundRunTime;
		this.simLog = SimulationLog.getInstance(settings.getCourseName());
		this.marom = simLog.getTeam("marom");
		this.ci_incidents = LogUtils.getCIsAndTheirIncidents();

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

			if (elapsed_time % roundRunTime == 0) {
				currentRound++;
				System.out.println("SimulationTester: " + elapsed_time + " current round is " + currentRound);
				nextRoundEnd += nextRoundEnd;
			}

			if (simLog.getIncidentTimes().containsKey(elapsed_time)) {

				int inc_id = simLog.getIncidentTimes().get(elapsed_time);
				int ci_id = marom.getIncident_logs().get(inc_id).getRoot_ci();

				marom.incidentStarted(inc_id, elapsed_time);
				System.out.println("SimulationTester: " + elapsed_time + " incident " + inc_id + " started. ci " + ci_id
						+ " is down. services " + simLog.getAffectingCis().get(ci_id) + " are down");
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
					solveService(sla_sol.getKey());
				}
			}
			marom.updateProfit(elapsed_time, 0);
		}

		/*
		 * Saving the target scores for each round as benchmark
		 */
		int roundRunTime = settings.getRunTime() * settings.getSessionsPerRound();

		ArrayList<Integer> targetScores = new ArrayList<>(settings.getRounds());
		for (int r = 1; r <= settings.getRounds(); r++) {
			int targetScore = marom.getProfits().get(roundRunTime * r).intValue();

			targetScores.add(targetScore);
		}

		for (int r = settings.getRounds(); r > 1; r--) {
			targetScores.set(r - 1, targetScores.get(r - 1) - targetScores.get(r - 2));
		}
		
		targetScores.set(0, (int) (targetScores.get(0)-settings.getInitCapital()));

		settings.setTargetScores(targetScores);
		LogUtils.saveSettings(settings);
		simLog.updateSettings(settings);
		System.out.println(targetScores);
		System.out.println(marom.getProfits());
	}

	/**
	 * Solves all CIs related to a certain service and updates the future SLA
	 * solutions
	 * 
	 * @param service_id
	 */
	private void solveService(int service_id) {
		HashSet<Integer> cis = simLog.getAffectedServices().get(service_id);
		for (Integer ci : cis) {
			HashSet<Integer> incidents = ci_incidents.get(ci);
			for (Integer inc : incidents) {
				if (marom.isIncidentOpen(inc, elapsed_time)) {
					simLog.incidentSolved("Marom", inc, elapsed_time, false);
					System.out.println("SimulationTester: " + elapsed_time + " ci " + ci + " is solved to fix service "
							+ service_id);
					updateSLASolutions(ci, elapsed_time);
				}
			}
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
				// the services is up
				if (solutions_schedule.containsKey(affService)) {
					// the service has a scheduled fix - removed
					solutions_schedule.remove(affService);
					System.out.println(
							"SimulationTester: " + elapsed_time + " fixing service " + affService + " is unscheduled");
				}
			} else {
				// the service is down
				int timeToFix = Math.min(services_sla.get(affService) + elapsed_time, nextRoundEnd);

				if (solutions_schedule.containsKey(affService)) {
					// a fix is already scheduled for the service
					int oldTimeToFix = solutions_schedule.get(affService);
					if (oldTimeToFix <= elapsed_time) {
						// the fix was/is already made - removed from schedule
						solutions_schedule.remove(affService);
						System.out.println(
								"SimulationTester: " + elapsed_time + " service " + affService + " is also fixed");
					} else {
						// the fix is scheduled to the future
						if (timeToFix < oldTimeToFix) {
							// reschedules the fix if sooner
							solutions_schedule.put(affService, timeToFix);
							System.out.println("SimulationTester: " + elapsed_time + " fixing service " + affService
									+ " is updated from " + oldTimeToFix + " to " + timeToFix);
						}
					}
				} else {
					// no fix is scheduled for the service. schedules a fix for
					// the service
					solutions_schedule.put(affService, timeToFix);
					System.out.println("SimulationTester: " + elapsed_time + " fixing service " + affService
							+ " is scheduled to " + timeToFix);
				}
			}
		}
	}
}
