package utils;

import java.util.HashMap;

import log.LogManager;
import log.Settings;

public class ClockIncrementor implements Runnable {
	private static volatile boolean isRunning = false;
	private static int elapsedTime;
	private static int remainingTime;
	private static int PAUSE_TIME;
	private static int ROUND_TIME;
	private static int sessionTime;
	private static int RUN_TIME;
	private static int elapsedRunTime;
	private static boolean isRunTime;
	private static ClockIncrementor instance;
	private static boolean isInitialized;

	public static void initialize(Settings settings, int currentRound) {

		if (isInitialized){
			return;
		}
		
		elapsedTime = (currentRound - 1) * settings.getRoundTime();
		elapsedRunTime = (currentRound - 1) * settings.getRunTime();
		remainingTime = settings.getPauseTime();
		PAUSE_TIME = settings.getPauseTime();
		sessionTime = settings.getSessionTime();
		RUN_TIME = settings.getRunTime();
		ROUND_TIME = settings.getRoundTime();
		isRunning = true;
		isRunTime = false;
		isInitialized = true;
	}

	private ClockIncrementor() {
		isInitialized = false;
	};

	public static ClockIncrementor getInstance() {
		if (instance == null) {
			instance = new ClockIncrementor();
		}
		return instance;
	}

	public static HashMap<String, Object> getClocks() {

		HashMap<String, Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedTime);
		clocks.put("remainingClock", remainingTime);
		clocks.put("elapsedRunTime", elapsedRunTime);
		clocks.put("isRunTime", isRunTime);
		System.out.println("remainingClock: " + remainingTime);
		return clocks;
	}

	public void run() {
		if (isRunning) {
//			System.out.println("ClockIncrementor : elapsed time= " + elapsedTime);
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
				
				if (elapsedTime % ROUND_TIME == 0){
					// finished round
					isRunning = false;
					LogManager.Stop(elapsedRunTime);
					return;
					
				} else{
					remainingTime = PAUSE_TIME;
					isRunTime = false;
					LogManager.pauseLog(elapsedRunTime, false);
				}
			}
		} else{
			return;
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
