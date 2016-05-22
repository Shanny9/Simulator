package utils;

import java.util.HashMap;

import log.LogManager;

public class ClockIncrementor implements Runnable {
	private static volatile boolean isRunning = false;
	private static int elapsedTime;
	private static int remainingTime;
	private static int finishRound;
	private static int round;
	private static int PAUSE_TIME;
	private static int sessionTime;
	private static int RUN_TIME;
	private static int elapsedRuntime;
	private static boolean isRunTime;

	public ClockIncrementor(int runTime, int roundTime, int currentRound, int pauseTime, int sessionT) {
		super();
		elapsedTime = 0;
		elapsedRuntime = 0;
		remainingTime = pauseTime;
		finishRound = roundTime * (round + 1);
		round = currentRound;
		PAUSE_TIME = pauseTime;
		sessionTime = sessionT;
		RUN_TIME = runTime;
		isRunning = true;
		isRunTime = false;
	}

	public static HashMap<String, Object> getClocks() {

		HashMap<String, Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedTime);
		clocks.put("remainingClock", remainingTime);
		clocks.put("elapsedRuntime", elapsedRuntime);
		return clocks;
	}

	public void run() {
		if (isRunning && elapsedTime < finishRound) {

			elapsedTime += 1;
			remainingTime -= 1;
			
			if (isRunTime){
				elapsedRuntime++;
			}

			if ((elapsedTime + RUN_TIME) % sessionTime == 0) {
				// finished pause time
				remainingTime = RUN_TIME;
				isRunTime = true;
				LogManager.resumeLog();

			} else if (elapsedTime % sessionTime == 0) {
				// finished run time
				remainingTime = PAUSE_TIME;
				isRunTime = false;
				LogManager.pauseLog(elapsedRuntime, false);
			}
		} else {
			LogManager.Stop(elapsedRuntime);
		}
	}

	public static int getRunTime() {
		return elapsedRuntime;
	}

	public static void forcePause() {
		isRunning = false;
		LogManager.pauseLog(elapsedRuntime, true);
	}

	public static void forceResume() {
		isRunning = true;
		LogManager.resumeLog();
	}

	public static boolean isRunning() {
		return isRunning;
	}
}
