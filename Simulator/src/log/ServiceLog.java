package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import utils.SimulationTime;

public class ServiceLog implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The profit/loss per second
	 */
	private double diff;
	/**
	 * Counts how many CI's the service depends on, are currently down
	 */
	private HashSet<Byte> cisDown;
	/**
	 * Even cells = UP, odd cells = DOWN
	 */
	private ArrayList<Integer> times;
	/**
	 * The profit lost every second when the service is up
	 */
	private double fixed_cost;
	/**
	 * The profit gained every second when the service is up
	 */
	private double fixed_income;
	/**
	 * The profit lost every second when the service is down
	 */
	private double down_cost;
	/**
	 * The accumulated profit lost for purchasing solutions
	 */
	private double purchase_cost;
	/**
	 * The status of the log
	 */
	private boolean isFinished;
	/**
	 * The service's ID
	 */
	private byte service_id;

	ServiceLog(byte id, double fixed_cost, double fixed_income, double down_cost) {
		super();
		this.service_id = id;
		this.cisDown = new HashSet<>();
		this.fixed_cost = fixed_cost;
		this.fixed_income = fixed_income;
		this.down_cost = down_cost;
		this.purchase_cost = 0;
		this.diff = fixed_income - fixed_cost;
		this.isFinished = false;
		this.times = new ArrayList<>();
		this.times.add(0);
	}

	/**
	 * Updates the service's status (up/ down)
	 * 
	 * @param time
	 *            The time of update
	 */
	synchronized void updateStatus(SimulationTime time) {
		addTime(time);
		diff = ((isUp()) ? (fixed_income - fixed_cost)
				: (-fixed_cost - down_cost));
	}

	private void addTime(SimulationTime time) {

		try {
			if (times.size() > 0 && time.before(getRoundDuration())) {
				throw new Exception("addTime exception: time ( " + time
						+ " ) is smaller than last time ( "
						+ getRoundDuration() + " )");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		times.add(time.getRunTime());
	}

	/**
	 * @return The service's gain/ loss of money per second
	 */
	synchronized double getDiff() {
		return diff;
	}

	/**
	 * Adds an end time to the service
	 * 
	 * @param time
	 *            The time of stop
	 */
	synchronized void stop(SimulationTime time) {
		if (!isFinished) {
			addTime(time);
			isFinished = true;
		}
	}

	/**
	 * @return The service's fixed cost
	 */
	synchronized double getFixed_cost() {
		return fixed_cost;
	}

	/**
	 * @return The service's fixed income
	 */
	synchronized double getFixed_income() {
		return fixed_income;
	}

	/**
	 * @return The service's down-time cost
	 */
	synchronized Double getDown_cost() {
		return down_cost;
	}

	/**
	 * @return The service's MTBF (Mean Time Between Failures)
	 */
	public synchronized Double getMTBF() {

		int failures = getNumOfFailures();
		if (failures == 0) {
			// returns the total up-time duration
			return (double) times.get(1);
		}

		return (double) getTotalUpTime() / failures;
	}

	public int getTotalUpTime() {
		int upTime = getRoundDuration() - getTotalDownTime();

		try {
			if (upTime < 0) {
				throw new Exception("getTotalUpTime exception: upTime ( "
						+ upTime + " ) is incorrect");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return upTime;
	}

	public double getUpTimePercentage() {
		int failures = getNumOfFailures();
		if (failures == 0) {
			// returns the total up-time duration
			return 1;
		}
		return (double) getTotalUpTime() / times.get(times.size() - 1);
	}

	/**
	 * @return The service's MTRS (Mean Time to Restore the Service)
	 */
	public synchronized double getMTRS() {

		int failures = getNumOfFailures();
		if (failures == 0) {
			// returns 0 because there were no failures
			return 0d;
		}

		return (double) getTotalDownTime() / failures;
	}

	/**
	 * @return The total duration that the services was down (also called TRS -
	 *         Time to Restore Service)
	 */
	int getTotalDownTime() {

		if (times.size() == 2) {
			// no failures
			return 0;
		}

		int totalDownTime = 0;
		for (int index = 2; index < times.size(); index += 2) {
			totalDownTime += times.get(index) - times.get(index - 1);
		}

		return totalDownTime;
	}

	/**
	 * @return The number of time the service was down
	 */
	int getNumOfFailures() {

		int numOfFailures = (times.size() - 1) / 2;

		try {
			if (numOfFailures < 0) {
				throw new Exception(
						"getNumOfFailures exception: numOfFailures ("
								+ numOfFailures + ") is negative");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return numOfFailures;
	}

	/**
	 * Updates the CI counter of the service, and, if needed, updates the
	 * service's status. The function returns the change in the service's profit
	 * gain speed.
	 */
	synchronized double ciUpdate(byte ci_id, boolean isUp, SimulationTime time) {
		double oldDiff = diff;
		if (isUp) {
			// if all CIs ARE DOWN, updates service status
			if (this.cisDown.remove(ci_id) && cisDown.size() == 0) {
				updateStatus(time);
			}
		} else {
			// if all CIs WERE DOWN, updates service status
			if (cisDown.size() == 0) {
				updateStatus(time);
			}
			this.cisDown.add(ci_id);
		}
		return diff - oldDiff;
	}

	/**
	 * @return The service's ID
	 */
	public byte getId() {
		return service_id;
	}

	/**
	 * Adds the amount to the service's purchase cost.
	 * 
	 * @param amount
	 *            The purchase price
	 */
	void purchase(double amount) {
		this.purchase_cost += amount;
	}

	/**
	 * @return {@code true} when the service is up. Otherwise return
	 *         {@code false}.
	 */
	synchronized public boolean isUp() {
		if (!isFinished) {
			return times.size() % 2 != 0;
		} else {
			return times.size() % 2 == 0;
		}
	}

	/**
	 * @return The round duration
	 */
	private int getRoundDuration() {
		return times.get(times.size() - 1);
	}

	/**
	 * @return The total gain (or loss) of the service.
	 */
	public double getGain() {
		double gain = getTotalUpTime() * getFixed_income();
		double fixed_loss = getRoundDuration() * getFixed_cost();
		double varinet_loss = getTotalDownTime() * getDown_cost();

		return gain - fixed_loss - varinet_loss - purchase_cost;
	}

	synchronized public String toString() {
		return "Service id= " + service_id + ", Times: " + times.toString();
	}
}