package utils;

import java.util.HashMap;

public class ClockIncrementor implements Runnable {
	private static volatile boolean stopThread = false;
	private static int elapsedClock;
	private static int remainingClock;
	private static int finishRound;
	private static int round;

	private static int pauseTime;
	private static int sessionTime;
	private static int runTime_;

	public ClockIncrementor(int runTime, int roundTime, int currentRound, int pause, int sessionT) {
		super();
		elapsedClock = 0;
		remainingClock = runTime;
		finishRound = roundTime * (round + 1);
		round = currentRound;
		pauseTime = pause;
		sessionTime = sessionT;
		runTime_ = runTime;
	}

	public static HashMap<String, Object> getClocks() {

		HashMap<String, Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedClock);
		clocks.put("remainingClock", remainingClock);
		return clocks;
	}

	public void run() {
		while (!stopThread && elapsedClock < finishRound) {
			synchronized (this) {
				try {
					System.out.println("elapsedClock: " + elapsedClock);
					System.out.println("finishRound: " + finishRound);
					System.out.println("remaining: " + remainingClock);
					System.out.println("----------------------");
					wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			elapsedClock += 1;
			remainingClock -= 1;

			if ((elapsedClock + pauseTime) % sessionTime == 0) {
				// finished runTime
//				log.SimulationLog.pause();
				remainingClock = pauseTime;

			} else if (elapsedClock % sessionTime == 0) {
				// finished pause time
				log.SimulationLog.resume();
				remainingClock = runTime_;
			}
		}
//		log.SimulationLog.Stop(elapsedClock);
	}

	public static void pause() {
		stopThread = true;
		log.SimulationLog.pause();
	}

	public static void resume() {
		stopThread = false;
		log.SimulationLog.resume();
	}
}
