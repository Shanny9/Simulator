package log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SimulationTester implements Runnable {
	private SimulationLog simLog;
	private int elapsed_time;
	private TeamLog marom;
	private int endTime;

	private HashMap<Integer, Integer> services_sla;
	public HashMap<Integer, Integer> sla_solutions;

	public SimulationTester() {
		super();
		simLog = SimulationLog.getInstance();
		marom = simLog.getTeam("marom");

		Settings settings = LogUtils.openSettings("Test");
		endTime = settings.getTotalRunTIme();

		HashMap<Integer, String> servicePriorities = LogUtils.getServicePriorities();
		HashMap<String, Integer> priority_sla = settings.getPriority_sla();

		sla_solutions = new HashMap<>();
		services_sla = new HashMap<>();

		for (Map.Entry<Integer, String> sp : servicePriorities.entrySet()) {
			services_sla.put(sp.getKey(), priority_sla.get(sp.getValue()));
		}

	}

	@Override
	public void run() {
		// TODO: fix loop condition
		while (++elapsed_time < endTime) {

			if (simLog.getIncidentTimes().containsKey(elapsed_time)) {

				int inc_id = simLog.getIncidentTimes().get(elapsed_time);
				int ci_id = marom.getIncident_logs().get(inc_id).getRoot_ci();

				marom.incidentStarted(inc_id, elapsed_time);
				updateSLASolutions(ci_id, elapsed_time);
			}

			// solves all the CIs at least one service relying them should be
			// solved
			// now according the SLA

			@SuppressWarnings("unchecked")
			HashMap<Integer, Integer> sla_solutions_copy = (HashMap<Integer, Integer>) LogUtils.copy(sla_solutions);
			for (Map.Entry<Integer, Integer> sla_sol : sla_solutions_copy.entrySet()) {
				if (sla_sol.getValue().intValue() == elapsed_time) {
					solve(sla_sol.getKey());
				}
			}

			marom.updateProfit(elapsed_time);

		}
	}

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
				sla_solutions.remove(affService);
			} else {
				sla_solutions.put(affService, services_sla.get(affService) + elapsed_time);
			}
		}
	}
}
