package utils;

import java.util.HashMap;

import log.LogManager;
import log.Settings;

public class ClockIncrementor implements Runnable {
	private static volatile boolean isRunning = false;
	private static Settings settings;
	private static int elapsedTime;
	private static int remainingTime;
	private static int PAUSE_TIME;
	private static int ROUND_TIME;
	private static int SESSION_TIME;
	private static int RUN_TIME;
	private static int elapsedRunTime;
	private static boolean isRunTime;
	private static ClockIncrementor instance;
	private static boolean isInitialized;

	public static void initialize(Settings _settings) {

		if (isInitialized){
			System.err.println("ClockIncrementor initialize method failed: ClockIncrementor is already running");
			return;
		}
		
		settings = _settings;

		// initializes constants
		PAUSE_TIME = settings.getPauseTime();
		SESSION_TIME = settings.getSessionTime();
		RUN_TIME = settings.getRunTime();
		ROUND_TIME = settings.getRoundTime();
		
		initVariables();
		
		isInitialized = true;
		System.out.println("ClockIncrementor: ClockIncrementor is initialized.");
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
		return clocks;
	}

	public void run() {
		if (isRunning) {
			elapsedTime += 1;
			remainingTime -= 1;

			if (isRunTime) {
				elapsedRunTime++;
			}

			if ((elapsedTime + RUN_TIME) % SESSION_TIME == 0) {
				// finished pause time
				remainingTime = RUN_TIME;
				isRunTime = true;
				LogManager.resumeLog();

			} else if (elapsedTime % SESSION_TIME == 0) {
				// finished run time
				
				if (elapsedTime % ROUND_TIME == 0){
					// finished round
					isRunning = false;
					LogManager.stop(elapsedRunTime);
					initVariables();
					System.out.println("ClockIncrementor: Round " + settings.getLastRoundDone()  + " is finished.");
					return;
					
				} else{
					// starts pause time
					remainingTime = PAUSE_TIME;
					isRunTime = false;
					LogManager.pauseLog(elapsedRunTime, false);
				}
			}
		} else{
			return;
		}
	}
	
	private static void initVariables(){
		elapsedTime = settings.getRoundTime();
		elapsedRunTime = settings.getRunTime();
		remainingTime = settings.getPauseTime();
		isRunning = true;
		isRunTime = false;
	}

	public static int getRunTime() {
		return elapsedRunTime;
	}

	public static void forcePause() {
		isRunning = false;
		LogManager.pauseLog(elapsedRunTime, true);
		System.out.println("ClockIncrementor: Paused (elapsed time is " + elapsedTime + " sec.)");
	}

	public static void forceResume() {
		isRunning = true;
		LogManager.resumeLog();
		System.out.println("ClockIncrementor: Resumed.");
	}

	public static boolean isRunning() {
		return isRunning;
	}
}
