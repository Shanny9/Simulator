package log;

import java.io.Serializable;
import java.util.HashSet;

public class SolutionLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String team;
	private HashSet<String> events;
	
	public SolutionLog(String courseName, String team, byte ci_id){
		this.team = team;
		this.events = SimulationLog.getInstance().getTimeEvents(ci_id);
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
