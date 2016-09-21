package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import utils.SimulationTime;

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
	private HashSet<Integer> roundsDone;
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
		this.roundsDone = new HashSet<>();
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
	 * @return The rounds done of the course.
	 */
	public HashSet<Integer> getRoundsDone() {
		return roundsDone;
	}

	/**
	 * @param round
	 *            the round to add.
	 */
	public void addRoundDone(int round) {
		this.roundsDone.add(round);
	}

	/**
	 * @param roundsDone
	 *            The rounds done of the course.
	 */
	public void setRoundsDone(Set<Integer> roundsDone) {
		this.roundsDone = new HashSet<>(roundsDone);
	}

	/**
	 * @return The Service Level Agreement (SLA) priorities.
	 */
	public HashMap<String, Integer> getPriority_sla() {
		return priority_sla;
	}

	/**
	 * @return The total <b>run time</b> of the simulation (including pause
	 *         times).
	 */
	public int getTotalRunTime() {
		return getRoundRunTime() * rounds;
	}

	/**
	 * @return The <b>total time</b> of the simulation (including pause times).
	 */
	public int getTotalTime() {
		return getRoundTime() * rounds;
	}

	/**
	 * 
	 * @return The <b>total time</b> of the round (including pause times).
	 */
	public int getRoundTime() {
		return roundTime;
	}

	/**
	 * @return The round <b>run time</b> (including pause times).
	 */
	public int getRoundRunTime() {
		return runTime * sessionsPerRound;
	}

	/**
	 * @return The session time.
	 */
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

	/**
	 * Sets the priority Service Level Agreement (SLA).
	 * 
	 * @param priority_sla
	 *            SLA priorities to set.
	 */
	public void setPriority_sla(HashMap<String, Integer> priority_sla) {
		this.priority_sla = priority_sla;
	}

	/**
	 * Strings a collection
	 * 
	 * @param Collection
	 *            <E> collection to string
	 * 
	 * @return A string of the collection.
	 */
	private <E> String stringCollection(Collection<E> col) {
		Object[] scores = col.toArray();
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

	/**
	 * Indicates if the round is valid.
	 * 
	 * @param round
	 *            Round to validate
	 * @return {@code true} whether the round is valid. Otherwise returns
	 *         {@code false}.
	 */
	private boolean validRound(int round) {
		return (round > 0 && round <= rounds);
	}

	/**
	 * Calculates the <b>run time</b> of the round end.
	 * 
	 * @param round
	 *            Round to check
	 * @return The run time end of the round.
	 */
	public SimulationTime getRoundRunTimeEnd(int round) {
		if (validRound(round)) {
			return new SimulationTime(getRoundRunTime() * round);
		}
		return null;
	}

	/**
	 * Calculates the <b>total time</b> of the round end (including pause
	 * times).
	 * 
	 * @param round
	 *            Round to check
	 * @return The end time of the round.
	 */
	public int getRoundTotalTimeEnd(int round) {
		if (validRound(round)) {
			return getRoundTime() * round;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Settings\n\nCourse name: [" + courseName + "]\nRounds: ["
				+ rounds + "]\nRun time [" + runTime
				+ "] seconds\nPause time: [" + pauseTime
				+ "] seconds\nSessions per round: [" + sessionsPerRound
				+ "]\nInitial capital: [" + initCapital
				+ "] NIS\nTarget scores: " + stringCollection(targetScores)
				+ "\nPriority_sla: " + printPrioritySLA() + "\nRounds done: "
				+ stringCollection(roundsDone);
	}

	/**
	 * Extracts the settings from a String (based on the {@code toString}
	 * method.
	 * 
	 * @param text
	 *            The text to extract the settings from.
	 * @return A new instance of {@code Settings}.
	 * @see Settings#toString
	 */
	public static Settings extractFromText(String text) {
		try {
			int start_bracket_index = text.indexOf("[");
			int end_bracket_index = text.indexOf("]");
			String exp;
			int expLen;
			ArrayList<String> expressions = new ArrayList<String>();
			do {
				exp = text
						.substring(start_bracket_index + 1, end_bracket_index);
				expressions.add(exp);
				text = text.substring(end_bracket_index + 1);

				start_bracket_index = text.indexOf("[");
				end_bracket_index = text.indexOf("]");
				expLen = end_bracket_index - start_bracket_index;
			} while (expLen > 0);

			String course = expressions.get(0);
			int rounds = Integer.parseInt(expressions.get(1));
			int runTime = Integer.parseInt(expressions.get(2));
			int pauseTime = Integer.parseInt(expressions.get(3));
			int sessionsPerRound = Integer.parseInt(expressions.get(4));
			int initialCapital = Integer.parseInt(expressions.get(5));

			String[] targetScores = expressions.get(6).replace(" ", "")
					.split(",");
			ArrayList<Integer> scores = new ArrayList<Integer>();
			for (int i = 0; i < targetScores.length; i++) {
				scores.add(Integer.parseInt(targetScores[i]));
			}
			HashMap<String, Integer> priority_sla = new HashMap<>();
			priority_sla.put("Low", Integer.parseInt(expressions.get(7)));
			priority_sla.put("Medium", Integer.parseInt(expressions.get(8)));
			priority_sla.put("High", Integer.parseInt(expressions.get(9)));
			priority_sla.put("Major", Integer.parseInt(expressions.get(10)));
			priority_sla.put("Critical", Integer.parseInt(expressions.get(11)));

			Set<Integer> roundsDone = new HashSet<>();
			String[] roundsDoneArr = expressions.get(12).split(",");

			if (roundsDoneArr != null && roundsDoneArr.length > 0 && !roundsDoneArr[0].equals("")) {
				for (int r = 0; r < roundsDoneArr.length; r++) {
					roundsDone.add(Integer.parseInt(roundsDoneArr[r]));
				}
			}
			
			Settings sett = new Settings(course, rounds, runTime, pauseTime,
					sessionsPerRound, initialCapital);
			sett.setRoundsDone(roundsDone);
			sett.setTargetScores(scores);
			sett.setPriority_sla(priority_sla);
			return sett;
		} catch (Exception e) {
			System.err
					.println("Settings: extractFromText method failed. File 'settings.txt' is corrupted.");
			return null;
		}
	}
}
