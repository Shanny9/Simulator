package utils;

import java.util.HashMap;

public class MoneyCalculator implements Runnable {
	private static int teamA;
	private static int teamB;

	public MoneyCalculator(int initProfit) {
		teamA = initProfit;
		teamB = initProfit;
	}

	public HashMap<String, Object> getProfits() {
		HashMap<String, Object> profits = new HashMap<>();
		profits.put("teamA", teamA);
		profits.put("teamB", teamB);
		return profits;
	}

	@Override
	public void run() {
		while (1 == 1) {

			synchronized (this) {
				try {
					wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// TODO: complete
		}
	}
}
