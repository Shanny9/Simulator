package log;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceLog implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * The last duration recorded
	 */
	private int lastDuration;
	/*
	 * The profit/loss per second
	 */
	private double diff;
	/*
	 * Counts how many CI's the service depends on, are currently down
	 */
	private int cisDown;
	/*
	 * Even cells = UP, odd cells = DOWN
	 */
	private ArrayList<Integer> times;
	/*
	 * The profit lost every second when the service is up
	 */
	private double fixed_cost;
	/*
	 * The profit gained every second when the service is up
	 */
	private double fixed_income;
	/*
	 * The profit lost every second when the service is down
	 */
	private double down_cost;
	/*
	 * The status of the log
	 */
	private boolean isFinished;
	/*
	 * The service's ID
	 */
	private int service_id;

	ServiceLog(int id, double fixed_cost, double fixed_income, double down_cost) {
		super();
		this.service_id = id;
		this.cisDown = 0;
		this.fixed_cost = fixed_cost;
		this.fixed_income = fixed_income;
		this.down_cost = down_cost;
		this.diff = fixed_income - fixed_cost;
		this.isFinished = false;
		this.times = new ArrayList<>();
		times.add(0);
	}

	void updateStatus(int time) {
		if (isFinished) {
			return;
		}
		times.add(time);
		
//		int timesSize = times.size();
//		this.lastDuration = times.get(timesSize - 1) - times.get(timesSize - 2);
		diff = ((isUp()) ? (fixed_income - fixed_cost) : (-fixed_cost - down_cost));

	}
	
	double getDiff(){
		return diff;
	}

	void stop(int time) {
		if (!isFinished) {
			times.add(time);
		}
		isFinished = true;
	}

	/**
	 * @return the fixed_cost
	 */
	double getFixed_cost() {
		return fixed_cost;
	}

	/**
	 * @return the fixed_income
	 */
	double getFixed_income() {
		return fixed_income;
	}

	/**
	 * @return the down_cost
	 */
	Double getDown_cost() {
		return down_cost;
	}

	Double getMTBF() {
		if (!isFinished) {
			return null;
		}

		int failures = (times.size() - 1) / 2;
		if (failures == 0) {
			// returns the whole time when the service was up
			return (double) times.get(1);
		}

		int totalUpTime = 0;
		for (int index = 2; index < times.size(); index += 2) {
			totalUpTime += times.get(index) - times.get(index - 1);
		}
		return (double) totalUpTime / failures;
	}

	Double getMTRS() {
		if (!isFinished) {
			return null;
		}

		int failures = (times.size() - 1) / 2;
		if (failures == 0) {
			// returns 0 because there were no failures
			return 0d;
		}

		int totalDownTime = 0;
		for (int index = 3; index < times.size(); index += 2) {
			totalDownTime += times.get(index) - times.get(index - 1);
		}
		return (double) totalDownTime / failures;
	}

	/*
	 * Updates the CI counter of the service, and, if needed, updates the
	 * service's status. The function returns the change in the service's profit
	 * gain speed.
	 */
	double ciUpdate(boolean isUp, int time) {
		double oldDiff = diff;
		if (isUp) {
			// if all CIs ARE DOWN, updates service status
			this.cisDown--;
			if (cisDown == 0) {
				updateStatus(time);
			}
		} else {
			// if all CIs WERE DOWN, updates service status
			if (cisDown == 0) {
				updateStatus(time);
			}
			this.cisDown++;
		}
		return oldDiff + diff;
	}
	
	int getId(){
		return service_id;
	}

	private boolean isUp() {
		if (!isFinished) {
			return times.size() % 2 != 0;
		} else {
			return times.size() % 2 == 0;
		}
	}
	
	public String toString(){
		return "Service id= " + service_id + ", Times: " + times.toString();
	}
}