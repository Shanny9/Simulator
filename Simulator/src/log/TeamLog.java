package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblGeneralParametersDaoImpl;
import com.daoImpl.TblServiceDaoImpl;
import com.model.TblCI;
import com.model.TblGeneral_parameter;
import com.model.TblService;
/*
 * TeamLog records the team's services, purchases and profits throughout the simulation
 */
public class TeamLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * The team's CI statuses: key=ci_id, value=isUP
	 */
	private HashMap<Integer, Boolean> cis;
	/*
	 * The team's services' logs
	 */
	private HashMap<Integer, ServiceLog> service_logs;
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
	/*
	 * The status of the log
	 */
	private boolean isFinished;

	/**
	 * @param initProfit
	 *            The team's initial profit
	 */
	TeamLog() {
		super();
		this.profits = new ArrayList<>();
		this.service_logs = new HashMap<>();
		this.purchases = new HashMap<>();
		this.isFinished = false;
		this.cis = new HashMap<>();
		
		this.profits.add(new TblGeneralParametersDaoImpl().getGeneralParameters().getInitialCapital());
		
		for (TblCI ci : new TblCIDaoImpl().getAllCIs()) {
			this.cis.put((int) ci.getCiId(), true);
		}

		List<TblService> services = new TblServiceDaoImpl().getAllServices();
		HashMap<Integer, Double> serviceDownTimeCosts = LogUtils.getServiceDownTimeCosts();

		for (TblService service : services) {
			int service_id = service.getServiceId();
			service_logs.put(service_id, new ServiceLog(service_id, service.getFixedCost(), service.getFixedIncome(),
					serviceDownTimeCosts.get(service_id)));
			this.diff += service_logs.get(service_id).getDiff();
		}
	}

	synchronized void updateCI(int ci_id, int time, boolean isBaught) {

		if (isFinished) {
			return;
		}

		long start = new Date().getTime();
		cis.put(ci_id, !cis.get(ci_id));
		HashSet<Integer> affectedServices = SimulationLog.getInstance().getAffectedServices().get(ci_id);

		for (Integer service_id : affectedServices) {
			ServiceLog affectedService = service_logs.get(service_id);
			diff += affectedService.ciUpdate(cis.get(ci_id), time);
		}
		if (isBaught) {
			purchases.put(time, ci_id);
			// reduces the solution cost from the team's profit at the given
			// @time
			profits.set(time, getProfit(time) - SimulationLog.getInstance().getCISolutionCost(ci_id));
		}
		long end = new Date().getTime();
		// checks if the calculation takes less than a second as it should
		System.out.println("updateCI calculation time: " + (end - start) / 1000 + "seconds");
	}

	public synchronized double getCurrentProfit() {
		return profits.get(profits.size() - 1);
	}

	synchronized double getProfit(int time) {
		return profits.get(time);
	}

	synchronized void updateProfit() {

		if (isFinished) {
			return;
		}
		profits.add(getCurrentProfit() + diff);
	}

	/**
	 * @return the service_logs
	 */
	synchronized HashMap<Integer, ServiceLog> getService_logs() {
		return service_logs;
	}

	HashMap<Integer, Integer> getPurchaces() {
		return purchases;
	}

	synchronized void Stop(int time) {
		for (ServiceLog service : service_logs.values()){
			service.stop(time);
		}
		this.isFinished = true;
	}
}
