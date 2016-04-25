package log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
	 * The team's incident logs
	 */
	private HashMap<Integer, IncidentLog> incident_logs;
	/*
	 * The team's service logs
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
		
		this.profits.add(new TblGeneralParametersDaoImpl().getGeneralParameters().getInitialCapital());
		List<TblService> services = new TblServiceDaoImpl().getAllServices();
		HashMap<Integer, Double> serviceDownTimeCosts = LogUtils.getServiceDownTimeCosts();
		
		for (TblService service : services) {
			int service_id = service.getServiceId();
			service_logs.put(service_id, new ServiceLog(service_id, service.getFixedCost(), service.getFixedIncome(),
					serviceDownTimeCosts.get(service_id)));
			this.diff += service_logs.get(service_id).getDiff();
		}
		incident_logs = LogUtils.getIncidentLogs();
	}

	synchronized void incidentSolved(int inc_id, int time, boolean isBaught) {

		if (isFinished) {
			return;
		}
		IncidentLog incLog = incident_logs.get(inc_id);
		incLog.setEnd_time(time);
		int ci_id = incLog.getRoot_ci();
		
		long start = new Date().getTime();
		HashSet<Integer> affectedServices = SimulationLog.getInstance().getAffectedServices().get(ci_id);

		for (Integer service_id : affectedServices) {
			diff += service_logs.get(service_id).ciUpdate(true, time);
		}
		
		if (isBaught) {
			purchases.put(time, ci_id);
			// reduces the solution cost from the team's profit at the given @time
			profits.set(time, getProfit(time) - SimulationLog.getInstance().getCISolutionCost(ci_id));
		}
		long end = new Date().getTime();
		// checks if the calculation takes less than a second as it should
		System.out.println("updateCI calculation time: " + (end - start) / 1000 + "seconds");
	}
	
	synchronized void incidentStarted(int inc_id, int time) {

		if (isFinished) {
			return;
		}
		
		int ci_id = incident_logs.get(inc_id).getRoot_ci();
				
		HashSet<Integer> affectedServices = SimulationLog.getInstance().getAffectedServices().get(ci_id);
		for (Integer service_id : affectedServices) {
			diff += service_logs.get(service_id).ciUpdate(false, time);
		}
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
	
	boolean isIncidentOpen(int inc_id, int time){
		return incident_logs.get(inc_id).isOpen(time);
	}
	
	public String toString(){
		String str = "Team Log\n========\n\nIncidents\n--------\n";
		for (IncidentLog il : incident_logs.values()){
			str +=il.toString() +"\n";
		}
		str+="\nServices\n--------\n";
		for (ServiceLog sl : service_logs.values()){
			str+=sl.toString() + "\n";
		}
		str+="\nPurchases\n--------\n";
		for (Map.Entry<Integer, Integer> entry : purchases.entrySet()){
			str+="Time= " + entry.getKey() + ", CI ID= " + entry.getValue() + "\n";
		}
		return str;
	}
}
