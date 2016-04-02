package utils;

import java.util.Date;

public class ClockManager implements Runnable {

	private Date elapsedClock;
	private Date remainingClock;
	private Date finishRound;
	private int round;

	@SuppressWarnings("deprecation")
	public ClockManager(int runTime, int roundTime, int currentRound) {

		elapsedClock = new Date(2016, 3, 30, 0, 0, 0);
		remainingClock = GeneralMethods.secToDate(runTime);
		finishRound = GeneralMethods.secToDate(roundTime*(currentRound + 1));
	}

	public Date getElapsedClock() {
		return elapsedClock;
	}

	public Date getRemainingClock() {
		return remainingClock;
	}

	public int getRound() {
		return round;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (elapsedClock.before(finishRound)) {
			try {
				wait(1000);
				elapsedClock.setSeconds(elapsedClock.getSeconds() + 1);
				remainingClock.setSeconds(remainingClock.getSeconds() - 1);
			} catch (InterruptedException e) {

			}
		}
	}
}
