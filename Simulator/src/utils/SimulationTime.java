package utils;

public class SimulationTime {

	private static int run_time;

	private static int pause_time;

	private static int sessions_in_round;

	private static int rounds;

	private static int totalSimulationRunTime;

	private int time;

	public static void initialize(int _run_time, int _pause_time,
			int _sessions_in_round, int _rounds) {
		run_time = _run_time;
		pause_time = _pause_time;
		sessions_in_round = _sessions_in_round;
		rounds = _rounds;
		totalSimulationRunTime = rounds * sessions_in_round * run_time;
	}

	public SimulationTime(int time) {
		if (isPositive() && !isTooBig()) {
			this.time = time;
		}
	}

	private boolean isPositive() {
		if (time < 0) {
			try {
				throw new Exception("SimulationTime: Time cannot be negative.");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private boolean isTooBig() {
		if (time > 3600) {
			try {
				throw new Exception(
						"SimulationTime: Time cannot exceed one day.");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		if (time > totalSimulationRunTime) {
			try {
				throw new Exception(
						"SimulationTime: Time cannot exceed the simulation's duration.");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public int getRound() {
		return time / rounds;
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
		time++;
		isTooBig();
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

	public String toString() {
		int hours = time / 3600;
		int minutes = (time % 3600) / 60;
		int seconds = time % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
