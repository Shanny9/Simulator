package log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.dao.TblCIDao;
import com.dao.TblCMDBDao;
import com.dao.TblEventDao;
import com.dao.TblIncidentDao;
import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblEevntDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.jdbc.DBUtility;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblEvent;
import com.model.TblIncident;
import com.model.TblSupplier;

import utils.Queries;

public class LogUtils {

	private static final String root_directory = "/logs/";
	private static final String date_time_foramt = "dd.MM.yy HH.mm";
	private static final String filePrefix = "round#";
	private static final boolean deleteHistory = true;

	/**
	 * Saves the log in the logs folder
	 * 
	 * @param courseName
	 *            The name of the course
	 * @param round
	 *            The round of the simulation
	 */
	public static void saveLog(String courseName, final int round) {
		try {
			String path = generatePath(courseName);
			File file = new File(path);
			file.mkdirs();

			final String newFileName = generateFileName(round);

			FileOutputStream fileOut = new FileOutputStream(path + newFileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(SimulationLog.getInstance());
			out.close();
			fileOut.close();
			System.out.printf("Log is saved in /tmp/log.ser");

			if (deleteHistory) {
				File[] matchingFiles = file.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.startsWith(filePrefix + round) && name.endsWith("ser") && !name.equals(newFileName);
					}
				});

				for (int i = 0; i < matchingFiles.length; i++) {
					matchingFiles[i].delete();
				}
			}

		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * Fetches the log from the logs folder
	 * 
	 * @param course
	 *            The name of the course
	 * @param round
	 *            The round of the simulation
	 * @return The simulation log given the course and the round
	 */
	public static SimulationLog openLog(String course, int round) {
		try {
			String path = generatePath(course);
			String fileName = generateFileName(round);

			FileInputStream fileIn = new FileInputStream(path + fileName);
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

	/**
	 * Generates a path given the course name
	 * 
	 * @param courseName
	 *            The name of the course
	 * @return The path to the directory of the course's log
	 */
	private static String generatePath(String courseName) {
		return root_directory + courseName + "/";
	}

	/**
	 * Generates a file name given the simulation's round
	 * 
	 * @param round
	 *            The simulation's round
	 * @return The file name of the requested log
	 */
	private static String generateFileName(int round) {
		String dateString = new SimpleDateFormat(date_time_foramt).format(new Date());
		return filePrefix + round + " - " + dateString + ".ser";
	}

	/**
	 * @return A map of the CI's and their affected services
	 */
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

	/**
	 * @return A map of the services and their affecting CIs
	 */
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

	/**
	 * @return The CIs and their costs
	 */
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

	/**
	 * @return The services' down-time costs
	 */
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
			System.out.println("getServiceDownTimeCosts(): " + e.getMessage());
		}
		return null;
	}

	/**
	 * @return Initialized incident logs
	 */
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

	/**
	 * @return The incidents and their timings
	 */
	static HashMap<Integer, Integer> getIncidentTimes() {
		HashMap<Integer, Integer> incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();
		for (TblIncident inc : dao.getAllIncidents()) {
			incidents.put(inc.getIncidentTime(), (int) inc.getIncidentId());
		}
		return incidents;
	}

	/**
	 * @return The incidents and their events
	 */
	public static HashMap<Integer, HashSet<String>> getIncidentEvents() {
		HashMap<Integer, HashSet<String>> incident_events = new HashMap<>();
		TblEventDao dao = new TblEevntDaoImpl();

		for (TblEvent event : dao.getAllEvents()) {
			int incident = event.getTblIncident().getIncidentId();
			HashSet<String> events = incident_events.get(incident);
			if (events == null) {
				events = new HashSet<String>();
			}
			events.add(String.valueOf(event.getEventId()));
			incident_events.put(incident, events);
		}
		return incident_events;
	}
}
