package utils;

import java.util.HashMap;

import log.SimulationLog;

public class ClockIncrementor implements Runnable {
	private static volatile boolean isRunning = false;
	private static int elapsedClock;
	private static int remainingClock;
	private static int finishRound;
	private static int round;
	private static int pauseTime;
	private static int sessionTime;
	private static int RUN_TIME;
	private static int runtime;

	public ClockIncrementor(int runTime, int roundTime, int currentRound, int pause, int sessionT) {
		super();
		elapsedClock = 0;
		runtime = 0;
		remainingClock = runTime;
		finishRound = roundTime * (round + 1);
		round = currentRound;
		pauseTime = pause;
		sessionTime = sessionT;
		RUN_TIME = runTime;
		isRunning = true;
	}

	public static HashMap<String, Object> getClocks() {

		HashMap<String, Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedClock);
		clocks.put("remainingClock", remainingClock);
		return clocks;
	}

	public void run() {
		if (isRunning && elapsedClock < finishRound) {
			
			elapsedClock += 1;
			remainingClock -= 1;
			runtime++;
			
			if ((elapsedClock + pauseTime) % sessionTime == 0) {
				// finished runTime
				remainingClock = pauseTime;
				runtime = 0;

			} else if (elapsedClock % sessionTime == 0) {
				// finished pause time
				remainingClock = RUN_TIME;
				SimulationLog.getInstance().fixAllIncidents(elapsedClock);
			}
		}
//		log.SimulationLog.Stop(elapsedClock);
	}
	
	public static int getCurrentRunTime(){
		return runtime;
	}

	public static void pause() {
		isRunning = false;
	}

	public static void resume() {
		isRunning = true;
	}
	
	public static boolean isRunning(){
		return isRunning;
	}
}
