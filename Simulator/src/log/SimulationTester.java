package log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import utils.SimulationTime;

public class SimulationTester implements Runnable {
	/**
	 * The simulation log - for accessing a team
	 */
	private static SimulationLog simLog;
	/**
	 * The simulation's elapsed time
	 */
	private static SimulationTime elapsed_time;
	/**
	 * A team for the simulation test
	 */
	private static TeamLog marom;
	/**
	 * The course's settings
	 */
	private static Settings settings;
	/**
	 * A map of services and their SLA times - key=service , value= max time to
	 * fix
	 */
	private static HashMap<Byte, Integer> services_sla;
	/**
	 * A schedule of future solutions - key=service, value=time to solve
	 */
	private static HashMap<Byte, Integer> solutions_schedule;

	private static int roundRunTime;
	
	private static int current_round;

	private static int nextRoundEnd;
	
	private static ArrayList<Integer> targetScores;

	private static SimulationTester instance;

	public static void initialize(Settings courseSettings) {

		if (!courseSettings.getTargetScores().isEmpty()) {
			System.err
					.println("SimulationTester initialize method failed: target scores are already calculated.");
			return;
		}
		current_round = 1;
		settings = courseSettings;
		SimulationTime.initialize(settings.getRunTime(),
				settings.getPauseTime(), settings.getSessionsPerRound(),
				settings.getRounds());
		elapsed_time = new SimulationTime(current_round);
		roundRunTime = settings.getRunTime() * settings.getSessionsPerRound();
		nextRoundEnd = roundRunTime;

		simLog = SimulationLog.getInstance();
		simLog.initialize(settings);
		simLog.setRound(1);

		marom = simLog.getTeam(SimulationLog.MAROM);

		HashMap<Byte, String> servicePriorities = LogUtils
				.getServicePriorities();
		HashMap<String, Integer> priority_sla = settings.getPriority_sla();

		solutions_schedule = new HashMap<>();
		services_sla = new HashMap<>();

		if (servicePriorities != null) {
			for (Map.Entry<Byte, String> sp : servicePriorities.entrySet()) {
				services_sla.put(sp.getKey(), priority_sla.get(sp.getValue()));
			}
		}
		
		targetScores = new ArrayList<>(settings.getRounds());
	}

	private SimulationTester() {
	}

	public static SimulationTester getInstance() {
		if (instance == null) {
			instance = new SimulationTester();
		}
		return instance;
	}

	@Override
	public void run() {
		while (elapsed_time.getRunTime() <= settings.getTotalRunTime()) {

			// 1. calculate the round and its end time
			if (elapsed_time.getRunTime() % roundRunTime == 0) {
				// new round
				int targetScore = marom.getProfits().get(roundRunTime-1).intValue();
				targetScores.add(targetScore);
				if (elapsed_time.getRunTime() == settings.getTotalRunTime()){
					break;
				}
				simLog.setRound(++current_round);
				marom = simLog.getTeam(SimulationLog.MAROM);
				nextRoundEnd += nextRoundEnd;
			}

			// 2. start incidents
			HashSet<Byte> affected_services = new HashSet<>();
			if (simLog.getIncidents().containsKey(elapsed_time)) {

				HashSet<Byte> ci_ids = simLog.getIncidents().get(elapsed_time);
				for (byte ci_id : ci_ids){
					affected_services.addAll(simLog.getAffectingCis().get(ci_id));
				}
				
				marom.incidentsStarted(ci_ids, elapsed_time);

//				if (simLog.getAffectingCis().get(ci_id) != null) {
					// System.out
					// .println("SimulationTester: " + elapsed_time
					// + " services "
					// + simLog.getAffectingCis().get(ci_id)
					// + " are down");
//				}
			}

			// 3. schedule services that are down
			if (affected_services != null && !affected_services.isEmpty()) {
				for (Byte aff_service : affected_services) {

					int time_to_solve = Math.min(elapsed_time.getRunTime()
							+ services_sla.get(aff_service), nextRoundEnd);
					if (solutions_schedule.putIfAbsent(aff_service,
							time_to_solve) == null) {
						// System.out.println("SimulationTester: " +
						// elapsed_time
						// + " service " + aff_service
						// + " fix is scheduled to " + time_to_solve);
					}
				}
			}

			// 4. convert service to CI and solve it
			// Set<Integer> services_to_solve =
			// solutions_schedule.entrySet().stream()
			// .filter(p -> p.getValue() == elapsed_time)
			// .collect(Collectors.toMap(p -> p.getKey(), p ->
			// p.getValue())).keySet();
			HashSet<Byte> services_to_solve = new HashSet<>();
			if (solutions_schedule != null) {
				for (Entry<Byte, Integer> e : solutions_schedule.entrySet()) {
					if (e.getValue().equals(elapsed_time))
						services_to_solve.add(e.getKey());
				}
			}
			if (services_to_solve != null && !services_to_solve.isEmpty()) {
				Set<Byte> cis_to_solve = new HashSet<>();
				for (Byte service : services_to_solve) {
					HashSet<Byte> affecting_cis = simLog.getAffectedServices()
							.get(service);
					if (affecting_cis != null) {
						cis_to_solve.addAll(affecting_cis);
					}
				}

				if (cis_to_solve != null && !cis_to_solve.isEmpty()) {
					for (Byte ci : cis_to_solve) {
						marom.ciSolved(ci, elapsed_time);
					}
					// System.out.println("SimulationTester: " + elapsed_time
					// + " services " + services_to_solve + " are solved");

					// 5. un-schedule services that are up
					for (Byte ci : cis_to_solve) {
						HashSet<Byte> fixed_services = simLog.getAffectingCis()
								.get(ci);
						// fixed_services.removeIf(s ->
						// !marom.getService_logs().get(s).isUp());
						@SuppressWarnings("unchecked")
						HashSet<Byte> cloned_fixed_services = (HashSet<Byte>) fixed_services
								.clone();
						if (fixed_services != null) {
							for (Byte fs : cloned_fixed_services) {
								if (marom.getService_log(fs).isUp()) {
									fixed_services.remove(fs);
								}
							}
						}

						if (fixed_services != null && !fixed_services.isEmpty()) {
							for (Byte fixed_service : fixed_services) {
								solutions_schedule.remove(fixed_service);
							}
						}
					}
				}
			}
			marom.updateProfit(elapsed_time, 0);
			elapsed_time.increment();
		}

		// 5. calculate the target scores and save settings
		
		targetScores.set(0,
				(int) (targetScores.get(0) + settings.getInitCapital()));
		
		for (int r = 1 ; r <settings.getRounds(); r++) {
			targetScores.set(r,
					targetScores.get(r) + targetScores.get(r-1));
		}

		settings.setTargetScores(targetScores);
		FilesUtils.saveSettings(settings);
		simLog.updateSettings(settings);
		System.out.println("SimulationTester: test is finished.");
	}
}
