package log;

import java.util.HashMap;

public class Team {
	private String teamName;
	private HashMap<Integer, CILogItem> ci_logs;
	private HashMap<Integer, ServiceLogItem> service_logs;
	/**
	 * @param teamName
	 */
	public Team(String teamName) {
		super();
		this.teamName = teamName;
		this.ci_logs = new HashMap<>();
		this.service_logs = new HashMap<>();
	}
	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}
	/**
	 * @return the ci_logs
	 */
	public HashMap<Integer, CILogItem> getCi_logs() {
		return ci_logs;
	}
	/**
	 * @return the service_logs
	 */
	public HashMap<Integer, ServiceLogItem> getService_logs() {
		return service_logs;
	}
	
		
	
}
