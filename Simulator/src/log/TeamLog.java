package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class TeamLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * The team's configuration items' logs
	 */
	private HashMap<Integer, CILogItem> ci_logs;
	/*
	 * The team's services' logs
	 */
	private HashMap<Integer, ServiceLogItem> service_logs;
	/*
	 * The team's list of purchases: key=time, value=ci_id
	 */
	private HashMap<Integer, Integer> purchases;
	/*
	 * The team's history of profits;
	 */
	private ArrayList<Double> profits;
	/*
	 * The team's profit gain per second (not including solution purchases)
	 */
	private double diff;

	/**
	 * @param initProfit
	 *            The team's initial profit
	 */
	public TeamLog(double initProfit, HashMap<Integer, CILogItem> ci_logs, HashMap<Integer, ServiceLogItem> service_logs) {
		super();
		this.profits.add(initProfit);
		this.ci_logs = ci_logs;
		this.service_logs = service_logs;
	}

	public synchronized void updateCI(int ci_id, int time, boolean isBaught) {

		long start = new Date().getTime();
		CILogItem cili = ci_logs.get(ci_id);
		cili.update(time);
		HashSet<Integer> affectedServices = Log.getInstance().getAffectedServices().get(ci_id);
		
		for (Integer service_id : affectedServices) {
			ServiceLogItem affectedService = service_logs.get(service_id);
			diff += affectedService.ciUpdate(cili.isUp(),time);	
		}
		if (isBaught){
			purchases.put(time, ci_id);
			// reduces the solution cost from the team's profit at the given @time
			profits.set(time, getProfit(time) - Log.getInstance().getCISoultionCost(ci_id));
		}
		long end = new Date().getTime();
		// checks if the calculation takes less than a second as it should
		System.out.println("updateCI calculation time: " + (end - start) / 1000 + "seconds");
	}

	public synchronized double getCurrentProfit() {
		return profits.get(profits.size() - 1);
	}

	public synchronized double getProfit(int time) {
		return profits.get(time);
	}

	public synchronized void updateProfit() {
		profits.add(getCurrentProfit() + diff);
	}

	/**
	 * @return the ci_logs
	 */
	public synchronized HashMap<Integer, CILogItem> getCi_logs() {
		return ci_logs;
	}

	/**
	 * @return the service_logs
	 */
	public synchronized HashMap<Integer, ServiceLogItem> getService_logs() {
		return service_logs;
	}

	public HashMap<Integer, Integer> getPurchaces() {
		return purchases;
	}
}
