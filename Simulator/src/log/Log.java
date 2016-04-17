package log;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Log {
	private static HashMap<Integer, HashSet<Integer>> affecting_cis;
	private static HashMap<Integer, HashSet<Integer>> affected_services;

	private static Log instance;

	private Team marom;
	private Team golan;

	/**
	 * @param cis
	 * @param services
	 */
	public Log(Team marom, Team golan) {
		super();
		this.marom = marom;
		this.golan = golan;
	}

	public static Log getInstance() {
		return instance;
	}

	public HashMap<Integer,CILogItem> getCILogs(String team) {
		if (team.equals("Marom")){
			return marom.getCi_logs();
		} else if (team.equals("Golan")){
			return golan.getCi_logs();
		}
		return null;
	}

	public HashMap<Integer,ServiceLogItem> getServiceLogs(String team) {
		if (team.equals("Marom")){
			return marom.getService_logs();
		} else if (team.equals("Golan")){
			return golan.getService_logs();
		}
		return null;
	}

	/**
	 * @return the cis
	 */
	public Collection<Integer> getServices(int ci) {
		return affected_services.keySet();
	}

	/**
	 * @return the services
	 */
	public Collection<Integer> getCis(int service) {
		return affecting_cis.keySet();
	}

	public void updateCILog(String team, int ci_id, int time) {
		
		CILogItem cili = getCILogs(team).get(ci_id);
		cili.updateStatus(time);
		for (Integer service_id : affecting_cis.get(ci_id)) {
			ServiceLogItem affectedService = getServiceLogs(team).get(service_id);
			// check if service goes down
			if (affectedService.isUp()) {
				 if (!cili.isUp()){
					 affectedService.updateStatus(time);
				 }
			} else{
				// check if service goes up
				if (cili.isUp()){
					boolean allAffectingAreUp = true;
					for (Integer ci : affected_services.get(service_id)){
						CILogItem affecting = getCILogs(team).get(ci);
						if (!affecting.isUp()){
							allAffectingAreUp = false;
							break;
						}
					}
					if (allAffectingAreUp){
						affectedService.updateStatus(time);
					}
				}
			}
		}
	}

}
