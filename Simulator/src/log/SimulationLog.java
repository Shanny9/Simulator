package log;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblGeneralParametersDaoImpl;

public class SimulationLog implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * key=ci_id, value=set of affected services
	 */
	private HashMap<Integer, HashSet<Integer>> affecting_cis;
	/*
	 * key=service_id, value=set of affecting CIs
	 */
	private HashMap<Integer, HashSet<Integer>> affected_services;
	/*
	 * key=ci_id, value=solution cost
	 */
	private HashMap<Integer, Double> ciSolCosts;
	
	private static SimulationLog instance;
	private TeamLog marom;
	private TeamLog golan;
	/*
	 * False if simulator is running
	 */
	private static boolean stopThread;
	
	private int roundTotalRunTime;

	/**
	 * @param cis
	 * @param servicesItems
	 */
	SimulationLog() {
		super();
		affecting_cis = LogUtils.getDBAffectingCIs();
		affected_services = LogUtils.getDBAffectedServices();
		roundTotalRunTime = new TblGeneralParametersDaoImpl().getRoundTotalRunTime();
		ciSolCosts = LogUtils.getCISolCosts();
		
		marom = new TeamLog();
		golan = new TeamLog();
	}

	public static SimulationLog getInstance() {
		if (instance == null) {
			System.out.println("Log is created");
			instance = new SimulationLog();
		}
		return instance;
	}

	public TeamLog getTeam(String team) {
		if (team.equals("Marom")) {
			return marom;
		} else if (team.equals("Golan")) {
			return golan;
		}
		return null;
	}

	HashMap<Integer, HashSet<Integer>> getAffectingCis() {
		return affecting_cis;
	}

	HashMap<Integer, HashSet<Integer>> getAffectedServices() {
		return affected_services;
	}

	double getCISolutionCost(int ci_id){
		return ciSolCosts.get(ci_id);
	}
	
	public void updateCILog(String team, int ci_id, int time, boolean isBaught) {
		getTeam(team).updateCI(ci_id, time, isBaught);
	}

	public static void pause() {
		stopThread = true;
		log.LogUtils.saveLog();
	}

	public static void resume() {
		stopThread = false;
	}
	
	int getRoundTotalRunTime(){
		return roundTotalRunTime;
	}

	@Override
	public void run() {
		while (!stopThread) {
			// should occur every second
			marom.updateProfit();
			golan.updateProfit();
		}
	}

}
