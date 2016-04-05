package utils;

import java.util.Date;
import java.util.HashMap;

public class ClockIncrementor implements Runnable {
	private static Date elapsedClock;
	private static Date remainingClock;
	private Date finishRound;
	private int round;
	
	@SuppressWarnings("deprecation")
	public ClockIncrementor (int runTime, int roundTime, int currentRound) {
		elapsedClock = new Date(70, 0, 1, 0, 0, 0);
		remainingClock = GeneralMethods.secToDate(runTime);
		this.finishRound = GeneralMethods.secToDate(roundTime*(currentRound + 1));
		this.round = currentRound;
	}
	
	public static HashMap<String, Object> getClocks(){
		HashMap<String,Object> clocks = new HashMap<>();
		clocks.put("elapsedClock", elapsedClock);
		clocks.put("remainingClock", remainingClock);
		return clocks;
	}
	
	@SuppressWarnings("deprecation")
	public void run(){
//		System.out.println("ClockIncrementor: started running");
		while (elapsedClock.before(finishRound)) {
			System.out.println("elapsedClock: "+elapsedClock);
			System.out.println("finishRound: "+ finishRound);
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
