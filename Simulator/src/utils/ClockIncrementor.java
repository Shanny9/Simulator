package utils;

import java.util.Date;
import java.util.HashMap;

public class ClockIncrementor implements Runnable {
	private static Date elapsedClock;
	private static Date remainingClock;
	private static Date finishRound;
	private static int round;
	
	public ClockIncrementor () {
		
	}
	
	@SuppressWarnings("deprecation")
	public static HashMap<String, Object> getClocks(int runTime, int roundTime, int currentRound){
		elapsedClock = new Date(1970, 0, 1, 0, 0, 0);

		
		remainingClock = GeneralMethods.secToDate(runTime);
		finishRound = GeneralMethods.secToDate(roundTime*(currentRound + 1));
		round = currentRound;
		
		HashMap<String,Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedClock);
		clocks.put("remainingClock", remainingClock);
		return clocks;
	}
	
	@SuppressWarnings("deprecation")
	public void run(){
		while (elapsedClock.before(finishRound)) {

				synchronized (this) {
					try {
						wait(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				elapsedClock.setSeconds(elapsedClock.getSeconds() + 1);
				remainingClock.setSeconds(remainingClock.getSeconds() - 1);
		}
	}
}
