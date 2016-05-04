package log;

import java.util.HashSet;

public class SolutionLog {
	private String team;
	private HashSet<String> events;
	
	public SolutionLog(String team, int incident_id){
		this.team = team;
		this.events = LogUtils.getIncidentEvents(incident_id);
	}
	
	public String getTeam(){
		return team;
	}
	
	public HashSet<String> getEvents(){
		return events;
	}
}
