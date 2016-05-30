package log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import org.apache.tomcat.util.http.fileupload.FileUtils;

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

	public static String path = "";
	private static final String date_time_foramt = "dd.MM.yy HH.mm";
	private static final String filePrefix = "round#";
	private static final boolean deleteHistory = true;

	/**
	 * Checks is a course has the 'settings.ser' file
	 * 
	 * @param courseName
	 *            The name of the course
	 * @return If 'settings.ser' file exists, returns number of rounds.
	 *         Otherwise returns 0
	 */
	public static int getCourseRounds(String courseName) {

		File file = new File(path + File.separator + courseName);
		file.mkdirs();

		File[] settingsFiles = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.equals("settings.ser");
			}
		});

		if (settingsFiles == null || settingsFiles.length == 0) {
			return 0;
		}
		return openSettings(courseName).getRounds();
	}

	public static void deleteCourse(String courseName) {

		File file = new File(path + File.separator + courseName);
		if (!file.exists()) {
			return;
		}

		try {
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return The names of the courses in the path
	 */
	public static String[] getCourses() {

		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return directories;
	}

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

			File file = new File(path + File.separator + courseName + "/");
			file.mkdirs();

			final String newFileName = generateFileName(round);

			FileOutputStream fileOut = new FileOutputStream(path + courseName + newFileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(SimulationLog.getInstance(courseName));
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
			String fileName = generateFileName(round);

			FileInputStream fileIn = new FileInputStream(path + File.separator + course + File.separator + fileName);
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
	 * Creates new directory for the course and saves "settings.ser" in it
	 * 
	 * @param settings
	 *            The course's settings
	 */
	public static void saveSettings(Settings settings) {

		File file = new File(path + File.separator + settings.getCourseName());
		file.mkdirs();

		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(
					path + File.separator + settings.getCourseName() + File.separator + "settings.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(settings);
			out.close();
			fileOut.close();
			System.out.println("settings for course " + settings.getCourseName() + " were saved in " + path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Opens the course's settings given its name
	 * 
	 * @param courseName
	 *            The course name
	 * @return The course's settings
	 */
	public static Settings openSettings(String courseName) {
		try {

			FileInputStream fileIn = new FileInputStream(
					path + File.separator + courseName + File.separator + "settings.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Settings settings = (Settings) in.readObject();
			in.close();
			fileIn.close();
			return settings;
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Log class not found");
			c.printStackTrace();
		}
		return null;
	}

	// /**
	// * Generates a path given the course name
	// *
	// * @param courseName
	// * The name of the course
	// * @return The path to the directory of the course's log
	// */
	// private static String generatePath(String courseName) {
	// return path + courseName + "/";
	// }

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
	static HashMap<Integer, Integer> getIncidentTimes(double mul) {
		HashMap<Integer, Integer> incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();
		for (TblIncident inc : dao.getAllIncidents()) {
			incidents.put((int) (inc.getIncidentTime() * mul), (int) inc.getIncidentId());
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

	static HashMap<Integer, String> getServicePriorities() {
		HashMap<Integer, String> servicePriority = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.servicePriorities);
			while (rs.next()) {
				servicePriority.put(rs.getInt("service_id"), rs.getString("priorityName"));
			}
			return servicePriority;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns a copy of the object, or null if the object cannot be serialized.
	 */
	public static Object copy(Object orig) {
		Object obj = null;
		try {
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(orig);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			obj = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}
}
