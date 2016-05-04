package log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

import com.dao.TblCIDao;
import com.dao.TblCMDBDao;
import com.dao.TblIncidentDao;
import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.jdbc.DBUtility;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblIncident;
import com.model.TblSupplier;

import utils.Queries;

public class LogUtils {

	public static void saveLog() {
		try {
			FileOutputStream fileOut = new FileOutputStream("log.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(SimulationLog.getInstance());
			out.close();
			fileOut.close();
			System.out.printf("Log is saved in /tmp/log.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static SimulationLog openLog() {
		try {
			FileInputStream fileIn = new FileInputStream("log.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			SimulationLog log = (SimulationLog) in.readObject();
			in.close(); 
			fileIn.close();
			return log;
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Log class not found");
			c.printStackTrace();
		}
		return null;
	}

	static HashMap<Integer, HashSet<Integer>> getDBAffectingCIs() {
		HashMap<Integer, HashSet<Integer>> dbAffectingCis = new HashMap<>();
		TblCMDBDao dao = new TblCMDBDaoImpl();
		
		for (TblCMDB cmdb : dao.getAllCMDBs()) {
			int ci = cmdb.getId().getCiId();
			HashSet<Integer> services = dbAffectingCis.get(ci);
			if (services == null) {
				services = new HashSet<Integer>();
			}
			services.add((int) cmdb.getId().getServiceId());
			dbAffectingCis.put(ci, services);
		}
		return dbAffectingCis;
	}

	static HashMap<Integer, HashSet<Integer>> getDBAffectedServices() {
		HashMap<Integer, HashSet<Integer>> dbAffectedServices = new HashMap<>();
		TblCMDBDao dao = new TblCMDBDaoImpl();
		
		for (TblCMDB cmdb : dao.getAllCMDBs()) {
			int service = cmdb.getId().getServiceId();
			HashSet<Integer> cis = dbAffectedServices.get(service);
			if (cis == null) {
				cis = new HashSet<Integer>();
			}
			cis.add((int) cmdb.getId().getCiId());
			dbAffectedServices.put(service, cis);
		}
		return dbAffectedServices;
	}

	static HashMap<Integer, Double> getCISolCosts() {
		HashMap<Integer, Double> ciSolCosts = new HashMap<>();
		TblCIDao dao = new TblCIDaoImpl();
		for (TblCI ci : dao.getAllCIs()) {
			int ci_id = ci.getCiId();
			String supName = ci.getTblSupplier2().getSupplierName();
			TblSupplier sup = new TblSupplierDaoImpl().getSupplierById(supName);
			int mul = 1;
			switch (sup.getCurrency()) {
			case "NIS":
				mul = 1;
				break;
			case "USD":
				mul = 4;
				break;
			case "EUR":
				mul = 5;
				break;
			}
			double solCost = sup.getSolutionCost() * mul;

			ciSolCosts.put(ci_id, solCost);
		}
		return ciSolCosts;
	}

	static HashMap<Integer, Double> getServiceDownTimeCosts() {
		HashMap<Integer, Double> serviceCosts = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.serviceDownCosts);
			while (rs.next()) {
				serviceCosts.put(rs.getInt("service_id"), rs.getDouble("down_cost"));
			}
			return serviceCosts;
		} catch (SQLException e) {
		}
		return null;
	}

	static HashMap<Integer, IncidentLog> getIncidentLogs() {
		HashMap<Integer, IncidentLog> incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();
		for (TblIncident inc : dao.getAllIncidents()) {
			int inc_id = (int) inc.getIncidentId();
			int ci_id = (int) inc.getCiId();
			int start_time = inc.getIncidentTime();
			incidents.put(inc_id, new IncidentLog(inc_id, ci_id, start_time));
		}
		return incidents;
	}

	static HashMap<Integer, Integer> getIncidentTimes() {
		HashMap<Integer, Integer> incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();
		for (TblIncident inc : dao.getAllIncidents()) {
			incidents.put(inc.getIncidentTime(), (int) inc.getIncidentId());
		}
		return incidents;
	}

	public static HashSet<String> getIncidentEvents(int incident_id) {
		HashSet<String> events = new HashSet<>();
		try {
			PreparedStatement stmt = DBUtility.getConnection().prepareStatement(Queries.getIncidentEvents);
			stmt.setInt(1, incident_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				events.add(String.valueOf(rs.getInt("event_id")));
			}
			return events;
		} catch (SQLException e) {
		}
		return null;
	}
}
