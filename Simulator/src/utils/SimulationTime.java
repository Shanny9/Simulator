package utils;

import java.io.Serializable;

public class SimulationTime implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int run_time;

	private static int pause_time;

	private static int sessions_in_round;

	private static int rounds;

	private static int totalSimulationRunTime;

	private static boolean isInitialized = false;

	private int time;

	public static void initialize(int _run_time, int _pause_time,
			int _sessions_in_round, int _rounds) {
		run_time = _run_time;
		pause_time = _pause_time;
		sessions_in_round = _sessions_in_round;
		rounds = _rounds;
		totalSimulationRunTime = rounds * sessions_in_round * run_time;
		isInitialized = true;
	}

	public SimulationTime(int round, int session, int timeInSession) {
		checkInitialized();
		if (round > 0 && session > 0 && timeInSession >= 0) {
			int t = (round - 1) * getRoundRunTime() + (session - 1) * run_time
					+ timeInSession;

			if (notNegative(t) && !isTooBig(t)) {
				this.time = t;
			}
		}
	}

	private void checkInitialized() {
		if (!isInitialized) {
			try {
				throw new Exception(
						"SimulationTime: SimulationTime is not initialized.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public SimulationTime(int time) {
		checkInitialized();
		if (notNegative(time) && !isTooBig(time)) {
			this.time = time;
		}
	}

	private boolean notNegative(int timeToCheck) {
		if (timeToCheck < 0) {
			try {
				throw new Exception("SimulationTime: Time cannot be negative ("
						+ timeToCheck + " < 0).");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private boolean isTooBig(int timeToCheck) {
		if (timeToCheck > 3600 * 24) {
			try {
				System.err
						.println("SimulationTime: Time cannot exceed one day ("
								+ timeToCheck + " > " + 3600 * 24 + ").");
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
		}

		if (timeToCheck > totalSimulationRunTime) {
			try {
				System.err
						.println("SimulationTime: Time cannot exceed the simulation's duration ("
								+ timeToCheck
								+ " > "
								+ totalSimulationRunTime
								+ ").");
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
		}
		return false;
	}

	public int getRemainingTime() {
		return getRoundTime() - getRunTimeInRound();
	}

	private int getRoundTime() {
		return getSessionTime() * sessions_in_round;
	}

	private int getSessionTime() {
		return run_time + pause_time;
	}

	public static int getRoundRunTime() {
		return run_time * sessions_in_round;
	}

	public int getRound() {
		return (int) Math.ceil(time / getRoundRunTime() + 1);
	}

	public int getSessionInRound() {
		return (getRunTimeInRound() / run_time) + 1;
	}

	public int getSession() {
		return (getRunTime() / run_time) + 1;
	}

	public int getRunTime() {
		return time;
	}

	public int getRunTimeInRound() {
		return time - ((getRound() - 1) * getRoundRunTime());

	}

	public int getTimeUntilSessionEnds() {
		return run_time - getRunTimeInRound() % run_time;
	}

	public int getTimeIncludingBreaks() {
		int numOfFullRunTimes = time / run_time;
		int fullSessionDuration = numOfFullRunTimes * (run_time + pause_time);
		int timeInSession = pause_time + time % run_time;
		return fullSessionDuration + timeInSession;
	}

	public int increment() {
		isTooBig(time + 1);
		time++;
		return time;
	}

	public boolean before(SimulationTime other) {
		return this.time < other.getRunTime();
	}

	public boolean after(SimulationTime other) {
		return this.time > other.getRunTime();
	}

	public boolean equals(SimulationTime other) {
		return this.time == other.getRunTime();
	}

	public boolean before(int other) {
		return before(new SimulationTime(other));
	}

	public boolean after(int other) {
		return after(new SimulationTime(other));
	}

	public boolean equals(int other) {
		return equals(new SimulationTime(other));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + time;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimulationTime other = (SimulationTime) obj;
		if (time != other.time)
			return false;
		return true;
	}

	public String toString() {
		int hours = time / 3600;
		int minutes = (time % 3600) / 60;
		int seconds = time % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
