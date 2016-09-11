package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Settings implements Serializable {
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
	private int initCapital;
	private int lastRoundDone;
	private int roundTime;
	private int sessionTime;
	private ArrayList<Integer> targetScores;
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
	public Settings(String courseName, int rounds, int runTime, int pauseTime,
			int sessionsPerRound, int initCapital) {
		super();
		this.courseName = courseName;
		this.date = new Date();
		this.rounds = rounds;
		this.runTime = runTime;
		this.pauseTime = pauseTime;
		this.sessionsPerRound = sessionsPerRound;
		this.initCapital = initCapital;
		this.lastRoundDone = 0;
		this.roundTime = (runTime + pauseTime) * sessionsPerRound;
		this.sessionTime = runTime + pauseTime;
		this.targetScores = new ArrayList<>();

		// TODO: should be input from user
		priority_sla = new HashMap<>();
		priority_sla.put("Low", 70);
		priority_sla.put("Medium", 60);
		priority_sla.put("High", 50);
		priority_sla.put("Major", 40);
		priority_sla.put("Critical", 30);
	}

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName
	 *            the courseName to set
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
	 * @param date
	 *            the date to set
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
	 * @param rounds
	 *            the rounds to set
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
	 * @param runTime
	 *            the runTime to set
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
	 * @param pauseTime
	 *            the pauseTime to set
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
	 * @param sessionsPerRound
	 *            the sessionsPerRound to set
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
	 * @param initCapital
	 *            the initCapital to set
	 */
	public void setInitCapital(int initCapital) {
		this.initCapital = initCapital;
	}

	/**
	 * @return the lastRoundDone
	 */
	public int getLastRoundDone() {
		return lastRoundDone;
	}

	/**
	 * @param lastRoundDone
	 *            the lastRoundDone to set
	 */
	public void setLastRoundDone(int lastRoundDone) {
		this.lastRoundDone = lastRoundDone;
	}

	public HashMap<String, Integer> getPriority_sla() {
		return priority_sla;
	}

	public int getTotalRunTime() {
		return runTime * sessionsPerRound * rounds;
	}

	public int getTotalTime() {
		return sessionTime * sessionsPerRound * rounds;
	}

	public int getRoundTime() {
		return roundTime;
	}

	public int getSessionTime() {
		return sessionTime;
	}

	/**
	 * @return the targetScore
	 */
	public ArrayList<Integer> getTargetScores() {
		return targetScores;
	}

	/**
	 * @param targetScore
	 *            the targetScore to set
	 */
	public void setTargetScores(ArrayList<Integer> targetScores) {
		this.targetScores = targetScores;
	}

	public void setPriority_sla(HashMap<String, Integer> priority_sla) {
		this.priority_sla = priority_sla;
	}

	private String printTargetScores() {
		Object[] scores = targetScores.toArray();
		return Arrays.toString(scores);
	}

	private String printPrioritySLA() {
		String output = "";
		ArrayList<String> priorityNames = new ArrayList<String>(
				Arrays.asList(new String[] { "Low", "Medium", "High", "Major",
						"Critical" }));
		for (String priority : priorityNames) {
			output += priority + ": [" + priority_sla.get(priority) + "]\n";
		}
		return output;
	}

	@Override
	public String toString() {
		return "Settings\n\nCourse name: [" + courseName + "]\nRounds: ["
				+ rounds + "]\nRun time [" + runTime
				+ "] seconds\nPause time: [" + pauseTime
				+ "] seconds\nSessions per round: [" + sessionsPerRound
				+ "]\nInitial capital: [" + initCapital
				+ "] NIS\nLast round done: [" + lastRoundDone
				+ "]\nTarget scores: " + printTargetScores()
				+ "\nPriority_sla: " + printPrioritySLA();
	}
}
