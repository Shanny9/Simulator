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
	
	public SimulationTime(int time) {
		if (!isInitialized){
			try {
				throw new Exception("SimulationTime: SimulationTime is not initialized.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (notNegative(time) && !isTooBig(time)) {
			this.time = time;
		}
	}

	private boolean notNegative(int timeToCheck) {
		if (timeToCheck < 0) {
			try {
				throw new Exception("SimulationTime: Time cannot be negative (" + timeToCheck + " < 0).");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private boolean isTooBig(int timeToCheck) {
		if (timeToCheck > 3600) {
			try {
				throw new Exception(
						"SimulationTime: Time cannot exceed one day (" + timeToCheck + " > 3600).");
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
		}

		if (timeToCheck > totalSimulationRunTime) {
			try {
				throw new Exception(
						"SimulationTime: Time cannot exceed the simulation's duration (" + timeToCheck + " > " + totalSimulationRunTime + ").");
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
		}
		return false;
	}

	public int getRound() {
		int roundRunTime = run_time * sessions_in_round;
		return (time / roundRunTime) + ((time % roundRunTime == 0) ? 0 : 1);
	}

	public int getSession() {
		int roundRunTime = run_time * sessions_in_round;
		int timeInRound = time - (rounds * roundRunTime);
		return timeInRound / run_time;
	}

	public int getRunTime() {
		return time;
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
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + time;
		return result;
	}

	/* (non-Javadoc)
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
