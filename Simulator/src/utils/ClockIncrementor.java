package utils;

import java.util.HashMap;

import log.LogManager;
import log.Settings;

public class ClockIncrementor implements Runnable {
	private static volatile boolean isRunning = false;
	private static int elapsedTime;
	private static int remainingTime;
	private static int finishRound;
	private static int PAUSE_TIME;
	private static int sessionTime;
	private static int RUN_TIME;
	private static int elapsedRunTime;
	private static boolean isRunTime;

	public ClockIncrementor(Settings settings, int currentRound) {
		super();
		elapsedTime = (currentRound - 1) * settings.getRoundTime();
		elapsedRunTime = (currentRound - 1) * settings.getRunTime();
		remainingTime = settings.getPauseTime();
		finishRound = currentRound * settings.getRoundTime();
		PAUSE_TIME = settings.getPauseTime();
		sessionTime = settings.getSessionTime();
		RUN_TIME = settings.getRunTime();
		isRunning = true;
		isRunTime = false;
	}

	public static HashMap<String, Object> getClocks() {

		HashMap<String, Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedTime);
		clocks.put("remainingClock", remainingTime);
		clocks.put("elapsedRunTime", elapsedRunTime);
		return clocks;
	}

	public void run() {
		if (isRunning && elapsedTime < finishRound) {

			elapsedTime += 1;
			remainingTime -= 1;

			if (isRunTime) {
				elapsedRunTime++;
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
				LogManager.pauseLog(elapsedRunTime, false);
			}
		} else {
			LogManager.Stop(elapsedRunTime);
		}
	}

	public static int getRunTime() {
		return elapsedRunTime;
	}

	public static void forcePause() {
		isRunning = false;
		LogManager.pauseLog(elapsedRunTime, true);
	}

	public static void forceResume() {
		isRunning = true;
		LogManager.resumeLog();
	}

	public static boolean isRunning() {
		return isRunning;
	}
}
