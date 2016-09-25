package log;

import java.io.Serializable;
import java.util.HashSet;

import utils.SimulationTime;

public class SolutionLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String team;
	private int round;
	private int sessionInRound;
	private HashSet<String> events;
	private static SimulationLog simLog = SimulationLog.getInstance();
	
	public SolutionLog(String courseName, String team, byte inc_id){
		this.team = team;
		this.events = simLog.getIncidentEvents(inc_id);
		SimulationTime sTime = simLog.getTimeOfIncident(inc_id);
		round = sTime.getRound();
		sessionInRound = sTime.getSessionInRound();
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
	
	public int getRound(){
		return round;
	}
	
	public int getSessionInRound(){
		return sessionInRound;
	}
}
