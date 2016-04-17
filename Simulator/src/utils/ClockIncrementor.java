package utils;

import java.util.Date;
import java.util.HashMap;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

public class ClockIncrementor implements Runnable {
	private static volatile boolean stopThread = false;
	private static int elapsedClock;
	private static int remainingClock;
	private static int finishRound;
	private static int round;
	
	private static int pauseTime;
	private static int sessionTime;
	private static int runTime_;

	
	public ClockIncrementor (int runTime, int roundTime, int currentRound, int pause, int sessionT) {
		super();
		elapsedClock = 0;
		remainingClock = runTime;
		finishRound = roundTime*(round + 1);
		System.out.println("ClockIncrementor has constructed");
		round = currentRound;
		pauseTime = pause;
		sessionTime = sessionT;
		runTime_ = runTime;
	}
	
	public static HashMap<String, Object> getClocks(){
		System.out.println("ClockIncrementor is getting clocks");

		HashMap<String,Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedClock);
		clocks.put("remainingClock", remainingClock);
//		System.out.println("Server: remainingClock: "+ remainingClock);
		return clocks;
	}
	
	public void run(){
		while (!stopThread && elapsedClock < (finishRound)) {
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
				elapsedClock = (elapsedClock + 1);
				remainingClock = (remainingClock - 1);
				
				if ((elapsedClock + pauseTime) % (sessionTime) == 0) {
					// finished runTime
					remainingClock = pauseTime;

				} else if (elapsedClock % (sessionTime) == 0) {
					// finished pause time
					remainingClock = runTime_;
				}
				
				
		}
	}
	
	public static void pause(){
		stopThread = true;
	}
	
	public static void resume(){
		stopThread = false;
	}
}
