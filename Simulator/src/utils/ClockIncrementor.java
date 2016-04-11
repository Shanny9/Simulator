package utils;

import java.util.Date;
import java.util.HashMap;

public class ClockIncrementor implements Runnable {
	private static Date elapsedClock;
	private static Date remainingClock;
	private static Date finishRound;
	private static int round;
	
	@SuppressWarnings("deprecation")
	public ClockIncrementor (int runTime, int roundTime, int currentRound) {
		super();
		elapsedClock = new Date(1970, 0, 1, 0, 0, 0);
		remainingClock = GeneralMethods.secToDate(runTime);
		finishRound = GeneralMethods.secToDate(roundTime*(round + 1));
		System.out.println("ClockIncrementor has constructed");
		round = currentRound;
	}
	
	public static HashMap<String, Object> getClocks(){
		System.out.println("ClockIncrementor is getting clocks");

		HashMap<String,Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedClock);
		clocks.put("remainingClock", remainingClock);
	//	System.out.println("Server: remainingClock: "+ remainingClock);
		return clocks;
	}
	
	@SuppressWarnings("deprecation")
	public void run(){
		while (elapsedClock.before(finishRound)) {
				synchronized (this) {
					try {
						System.out.println("elapsedClock: " + elapsedClock);
						System.out.println("finishRound: " + finishRound);
						System.out.println("----------------------");
						wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				elapsedClock.setSeconds(elapsedClock.getSeconds() + 1);
				remainingClock.setSeconds(remainingClock.getSeconds() - 1);
		}
	}
}
