package log;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Settings implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String courseName;
	private Date date;
	private int rounds;
	private int runTime;
	private int pauseTime;
	private int sessionsPerRound;
	private double initCapital;
	private int lastRoundDone;
	private int roundTime;
	private int sessionTime;
	
	/**
	 * The priority name and MAX time to solve according to the SLA
	 */
	private HashMap<String, Integer> priority_sla;
	
	/**
	 * @param courseName
	 * @param date
	 * @param rounds
	 * @param runTime
	 * @param pauseTime
	 * @param sessionsPerRound
	 */
	public Settings(String courseName, int rounds, int runTime, int pauseTime, int sessionsPerRound, double initCapital) {
		super();
		this.courseName = courseName;
		this.date = new Date();
		this.rounds = rounds;
		this.runTime = runTime;
		this.pauseTime = pauseTime;
		this.sessionsPerRound = sessionsPerRound;
		this.initCapital = initCapital;
		this.lastRoundDone = 0;
		this.roundTime = (runTime+pauseTime)*sessionsPerRound*rounds;
		this.sessionTime = runTime + pauseTime;
		
		//TODO: should be input from user
		priority_sla = new HashMap<>();
		priority_sla.put("Low", 75);
		priority_sla.put("Medium", 60);
		priority_sla.put("High", 45);
		priority_sla.put("Major", 30);
		priority_sla.put("Critical", 15);
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the rounds
	 */
	public int getRounds() {
		return rounds;
	}
	/**
	 * @param rounds the rounds to set
	 */
	public void setRounds(int rounds) {
		this.rounds = rounds;
	}
	/**
	 * @return the runTime
	 */
	public int getRunTime() {
		return runTime;
	}
	/**
	 * @param runTime the runTime to set
	 */
	public void setRunTime(int runTime) {
		this.runTime = runTime;
	}
	/**
	 * @return the pauseTime
	 */
	public int getPauseTime() {
		return pauseTime;
	}
	/**
	 * @param pauseTime the pauseTime to set
	 */
	public void setPauseTime(int pauseTime) {
		this.pauseTime = pauseTime;
	}
	/**
	 * @return the sessionsPerRound
	 */
	public int getSessionsPerRound() {
		return sessionsPerRound;
	}
	/**
	 * @param sessionsPerRound the sessionsPerRound to set
	 */
	public void setSessionsPerRound(int sessionsPerRound) {
		this.sessionsPerRound = sessionsPerRound;
	}
	/**
	 * @return the initCapital
	 */
	public double getInitCapital() {
		return initCapital;
	}
	/**
	 * @param initCapital the initCapital to set
	 */
	public void setInitCapital(double initCapital) {
		this.initCapital = initCapital;
	}
	/**
	 * @return the lastRoundDone
	 */
	public int getLastRoundDone() {
		return lastRoundDone;
	}
	/**
	 * @param lastRoundDone the lastRoundDone to set
	 */
	public void setLastRoundDone(int lastRoundDone) {
		this.lastRoundDone = lastRoundDone;
	}
	
	public HashMap<String, Integer> getPriority_sla(){
		return priority_sla;
	}
	
	public int getTotalRunTIme(){
		return runTime*sessionsPerRound*rounds;
	}
	
	public int getRoundTime(){
		return roundTime;
	}
	public int getSessionTime() {
		return sessionTime;
	}
	
	
}
