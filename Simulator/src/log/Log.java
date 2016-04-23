package log;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblGeneralParametersDaoImpl;

public class Log implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, HashSet<Integer>> affecting_cis;
	private HashMap<Integer, HashSet<Integer>> affected_services;
	private HashMap<Integer, Double> ciSolutionCosts;
	private static Log instance;

	private TeamLog marom;
	private TeamLog golan;
	/*
	 * False if simulator is running
	 */
	private static boolean stopThread;

	/**
	 * @param cis
	 * @param services
	 */
	Log() {
		super();
		affecting_cis = LogUtils.getDBAffectingCIs();
		affected_services = LogUtils.getDBAffectedServices();
		ciSolutionCosts = new TblCIDaoImpl().getSolutionCosts();
		double initCapital = new TblGeneralParametersDaoImpl().getGeneralParameters().getInitialCapital();
		
		HashMap<Integer, CILogItem> ciItems = LogUtils.getDBSCIItems();
		HashMap<Integer, ServiceLogItem> serviceItems = LogUtils.getDBServiceItems();
		
		marom = new TeamLog(initCapital, ciItems, serviceItems);
		golan = new TeamLog(initCapital, ciItems, serviceItems);
	}

	public static Log getInstance() {
		if (instance == null) {
			System.out.println("Log is created");
			instance = new Log();
		}
		return instance;
	}

	public double getCISoultionCost(int ci_id) {
		return ciSolutionCosts.get(ci_id);
	}

	public TeamLog getTeam(String team) {
		if (team.equals("Marom")) {
			return marom;
		} else if (team.equals("Golan")) {
			return golan;
		}
		return null;
	}

	public HashMap<Integer, HashSet<Integer>> getAffectingCis() {
		return affecting_cis;
	}

	public HashMap<Integer, HashSet<Integer>> getAffectedServices() {
		return affected_services;
	}

	/**
	 * @return the cis
	 */
	public Collection<Integer> getServices(int ci) {
		return affected_services.keySet();
	}

	/**
	 * @return the services
	 */
	public Collection<Integer> getCis(int service) {
		return affecting_cis.keySet();
	}

	public void updateCILog(String team, int ci_id, int time, boolean isBaught) {
		getTeam(team).updateCI(ci_id, time, isBaught);
	}

	public static void pause() {
		stopThread = true;
	}

	public static void resume() {
		stopThread = false;
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
