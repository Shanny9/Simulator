package log;

import java.util.HashSet;

public class SolutionLog {
	private String team;
	private HashSet<String> events;
	
	public SolutionLog(String team, int inc_id){
		this.team = team;
		this.events = SimulationLog.getInstance().getIncidentEvents(inc_id);
	}
	
	/**
	 * @return The team that solved
	 */
	public String getTeam(){
		return team;
	}
	
	/**
	 * @return The events fixed by the solution
	 */
	public HashSet<String> getEvents(){
		return events;
	}
}
