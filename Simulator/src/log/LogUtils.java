package log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import utils.Queries;

import com.dao.TblCIDao;
import com.dao.TblCMDBDao;
import com.dao.TblEventDao;
import com.dao.TblIncidentDao;
import com.dao.TblServiceDao;
import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblEevntDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblServiceDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.jdbc.DBUtility;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblEvent;
import com.model.TblIncident;
import com.model.TblService;
import com.model.TblSupplier;

public class LogUtils {

	private static String path = "";
	private static final String date_time_foramt = "dd.MM.yy HH.mm";
	private static final String filePrefix = "round#";
	// TODO: ask user if he wants to override existing logs
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

		File file = new File(getPath() + File.separator + courseName);
		if (file.exists() && file.isDirectory()) {

			File[] settingsFiles = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.equals("settings.ser");
				}
			});

			if (settingsFiles == null || settingsFiles.length == 0) {
				return 0;
			}
			return openSettings(courseName).getRounds();
		} else
			return 0;
	}

	public static void deleteCourse(String courseName) {

		File file = new File(getPath() + File.separator + courseName);
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

		File file = new File(getPath());
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

			File file = new File(getPath() + File.separator + courseName
					+ File.separator);
			file.mkdirs();

			final String newFileName = generateFileName(round);

			FileOutputStream fileOut = new FileOutputStream(getPath()
					+ File.separator + courseName + File.separator
					+ newFileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			SimulationLog simLog = SimulationLog.getInstance();
			Settings sett = simLog.getSettings();
			int lastRoundDone = Math.max(sett.getLastRoundDone(), round);
			simLog.getSettings().setLastRoundDone(lastRoundDone);
			LogUtils.saveSettings(sett);
			out.writeObject(simLog);
			out.close();
			fileOut.close();
			System.out.printf("Log is saved");

			if (deleteHistory) {
				File[] matchingFiles = file.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.startsWith(filePrefix + round)
								&& name.endsWith("ser")
								&& !name.equals(newFileName);
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
	public static SimulationLog openLog(String course, final int round) {
		try {
			// checks that the log file exists
			File file = new File(getPath() + File.separator + course);
			File[] matchingFiles = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith(filePrefix + round)
							&& name.endsWith("ser");
				}
			});
			
			if (matchingFiles == null || matchingFiles.length == 0) {
				// TODO: tell the user log file is missing
				return null;
			}
			
			FileInputStream fileIn = new FileInputStream(matchingFiles[0]);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			SimulationLog log = (SimulationLog) in.readObject();
			
			//REMOVE THIS
			TeamLog marom = log.getTeam(true);
			
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

		File file = new File(getPath() + File.separator
				+ settings.getCourseName());
		file.mkdirs();

		FileOutputStream fos;
		try {
			String fullPath = getPath() + File.separator
					+ settings.getCourseName() + File.separator
					+ "settings.ser";
			fos = new FileOutputStream(fullPath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(settings);
			oos.close();
			fos.close();

			fullPath = fullPath.replace("settings.ser", "settings.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(fullPath));
			String txt = settings.toString().replace("\n",
					System.lineSeparator());
			bw.write(txt);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("settings for course " + settings.getCourseName()
				+ " were saved in " + getPath());
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
			// checks if course directory exists
			File file = new File(getPath() + File.separator + courseName);
			if (!file.exists() || !file.isDirectory()) {
				// TODO: tell the user the course directory is missing
				return null;
			}

			// checks for settings.ser in the course directory
			File[] settingsFiles = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.equals("settings.ser")
							|| name.equals("settings.txt");
				}
			});

			if (settingsFiles == null || settingsFiles.length == 0) {
				// TODO: tell the user settings.ser is missing
				return null;
			}

			String fileName = settingsFiles[0].getName();
			String fullPath = getPath() + File.separator + courseName
					+ File.separator + fileName;

			if (fileName.endsWith(".ser")) {
				FileInputStream fileIn = new FileInputStream(fullPath);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				Settings settings = (Settings) in.readObject();
				in.close();
				fileIn.close();
				return settings;

			} else if (fileName.endsWith(".txt")) {
				fullPath = fullPath.replace("settings.ser", "settings.txt");
				try (BufferedReader buffer = new BufferedReader(new FileReader(
						fullPath))) {
					String line;
					String input = "";
					while ((line = buffer.readLine()) != null) {
						input += line;
					}

					int start_bracket_index = input.indexOf("[");
					int end_bracket_index = input.indexOf("]");
					String exp;
					int expLen;
					ArrayList<String> expressions = new ArrayList<String>();
					do {
						exp = input.substring(start_bracket_index + 1,
								end_bracket_index);
						expressions.add(exp);
						input = input.substring(end_bracket_index + 1);

						start_bracket_index = input.indexOf("[");
						end_bracket_index = input.indexOf("]");
						expLen = end_bracket_index - start_bracket_index;
					} while (expLen > 0);
					
					String course = expressions.get(0);
					int rounds = Integer.parseInt(expressions.get(1));
					int runTime = Integer.parseInt(expressions.get(2));
					int pauseTime = Integer.parseInt(expressions.get(3));
					int sessionsPerRound = Integer.parseInt(expressions.get(4));
					int initialCapital = Integer.parseInt(expressions.get(5));
					int lastRoundDone = Integer.parseInt(expressions.get(6));
					String[] targetScores = expressions.get(7).replace(" ", "")
							.split(",");
					ArrayList<Integer> scores = new ArrayList<Integer>();
					for (int i = 0; i < targetScores.length; i++) {
						scores.add(Integer.parseInt(targetScores[i]));
					}
					HashMap<String, Integer> priority_sla = new HashMap<>();
					priority_sla.put("Low",
							Integer.parseInt(expressions.get(8)));
					priority_sla.put("Medium",
							Integer.parseInt(expressions.get(9)));
					priority_sla.put("High",
							Integer.parseInt(expressions.get(10)));
					priority_sla.put("Major",
							Integer.parseInt(expressions.get(11)));
					priority_sla.put("Critical",
							Integer.parseInt(expressions.get(12)));

					Settings sett = new Settings(course, rounds, runTime,
							pauseTime, sessionsPerRound, initialCapital);
					sett.setLastRoundDone(lastRoundDone);
					sett.setTargetScores(scores);
					sett.setPriority_sla(priority_sla);
					LogUtils.saveSettings(sett);

					return sett;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException | ClassNotFoundException i) {
			// TODO: tell the user reading the file has failed. May occur if the
			// Settings class has changed.
			i.printStackTrace();
			return null;
		}
		return null;

	}

	/**
	 * Generates a file name given the simulation's round
	 * 
	 * @param round
	 *            The simulation's round
	 * @return The file name of the requested log
	 */
	private static String generateFileName(int round) {
		String dateString = new SimpleDateFormat(date_time_foramt)
				.format(new Date());
		return filePrefix + round + " - " + dateString + ".ser";
	}

	/**
	 * @return A map of the CI's and their affected services
	 */
	static HashMap<Byte, HashSet<Byte>> getDBAffectingCIs() {
		HashMap<Byte, HashSet<Byte>> dbAffectingCis = new HashMap<>();
		TblCMDBDao dao = new TblCMDBDaoImpl();

		for (TblCMDB cmdb : dao.getAllCMDBs()) {
			byte ci = cmdb.getCiId();
			HashSet<Byte> services = dbAffectingCis.get(ci);
			if (services == null) {
				services = new HashSet<Byte>();
			}
			services.add((byte) cmdb.getServiceId());
			dbAffectingCis.put(ci, services);
		}
		return dbAffectingCis;
	}

	/**
	 * @return A map of CIs and their incidents
	 */
	static HashMap<Byte, HashSet<Byte>> getCIsAndTheirIncidents() {
		HashMap<Byte, HashSet<Byte>> ci_incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();

		for (TblIncident incident : dao.getAllIncidents()) {
			byte ci = incident.getCiId();
			HashSet<Byte> incidents = ci_incidents.get(ci);
			if (incidents == null) {
				incidents = new HashSet<Byte>();
			}
			incidents.add(incident.getIncidentId());
			ci_incidents.put(ci, incidents);
		}
		return ci_incidents;
	}

	/**
	 * @return A map of the services and their affecting CIs
	 */
	static HashMap<Byte, HashSet<Byte>> getDBAffectedServices() {
		HashMap<Byte, HashSet<Byte>> dbAffectedServices = new HashMap<>();
		TblCMDBDao dao = new TblCMDBDaoImpl();

		for (TblCMDB cmdb : dao.getAllCMDBs()) {
			byte service = cmdb.getServiceId();
			HashSet<Byte> cis = dbAffectedServices.get(service);
			if (cis == null) {
				cis = new HashSet<Byte>();
			}
			cis.add(cmdb.getCiId());
			dbAffectedServices.put(service, cis);
		}
		return dbAffectedServices;
	}

	/**
	 * @return The CIs and their costs
	 */
	static HashMap<Byte, Double> getCISolCosts() {
		HashMap<Byte, Double> ciSolCosts = new HashMap<>();
		TblCIDao dao = new TblCIDaoImpl();
		for (TblCI ci : dao.getAllCIs()) {
			byte ci_id = ci.getCiId();
			String supName = ci.getSupplierName1();
			TblSupplier sup = new TblSupplierDaoImpl().getSupplierById(supName);
			int mul = 1;
			switch (sup.getCurrency()) {
			// TODO: hard-coded currencies and values. Can be stored somewhere
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
	static HashMap<Byte, Double> getServiceDownTimeCosts() {
		HashMap<Byte, Double> serviceCosts = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.serviceDownCosts);
			while (rs.next()) {
				serviceCosts.put(rs.getByte("service_id"),
						rs.getDouble("down_cost"));
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
	static HashMap<Byte, IncidentLog> getIncidentLogs() {
		HashMap<Byte, IncidentLog> incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();
		for (TblIncident inc : dao.getAllIncidents()) {
			byte inc_id = (byte) inc.getIncidentId();
			byte ci_id = (byte) inc.getCiId();
			int start_time = inc.getIncidentTime();
			incidents.put(inc_id, new IncidentLog(inc_id, ci_id, start_time));
		}
		return incidents;
	}

	/**
	 * @return The incidents and their timings (key= time, value= incident)
	 */
	static HashMap<Integer, Byte> getIncidentTimes(double mul) {
		HashMap<Integer, Byte> incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();
		for (TblIncident inc : dao.getAllIncidents()) {
			incidents.put((int) (inc.getIncidentTime() * mul),
					(byte) inc.getIncidentId());
		}
		return incidents;
	}

	/**
	 * @return The incidents and their events (key= incident, value= set of events)
	 */
	public static HashMap<Byte, HashSet<String>> getIncidentEvents() {
		HashMap<Byte, HashSet<String>> incident_events = new HashMap<>();
		TblEventDao dao = new TblEevntDaoImpl();

		for (TblEvent event : dao.getAllEvents()) {
			byte incident = event.getIncidentId();
			HashSet<String> events = incident_events.get(incident);
			if (events == null) {
				events = new HashSet<String>();
			}
			events.add(String.valueOf(event.getEventId()));
			incident_events.put(incident, events);
		}
		return incident_events;
	}
	
	/**
	 * @return The services and their affecting incidents (key= services, value= affecting incidents)
	 */
	public static HashMap<Byte, HashSet<Byte>> getServiceIncidents() {
		HashMap<Byte, HashSet<Byte>> service_incidents = new HashMap<>();
		TblServiceDao dao = new TblServiceDaoImpl();
		HashMap<Byte, HashSet<Byte>> affected_services = LogUtils.getDBAffectedServices();
		HashMap<Byte, HashSet<Byte>> ci_incidents = LogUtils.getCIsAndTheirIncidents();
		
		for (TblService service : dao.getAllServices()) {
			HashSet<Byte> incidents = service_incidents.get(service.getServiceId());
			if (incidents == null) {
				incidents = new HashSet<Byte>();
			}
			
			HashSet<Byte> affecting_cis = affected_services.get(service.getServiceId());
			if (affecting_cis == null){
				continue;
			}
			
			HashSet<Byte> affecting_incidents = new HashSet<>();
			for (Byte ci : affecting_cis){
				affecting_incidents.addAll(ci_incidents.get(ci));
			}
			
			incidents.addAll(affecting_incidents);
			service_incidents.put(service.getServiceId(), incidents);
		}
		return service_incidents;
	}

	/**
	 * @return A map of services and their priorities (key= service, value= priority name)
	 */
	static HashMap<Byte, String> getServicePriorities() {
		HashMap<Byte, String> servicePriority = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.servicePriorities);
			while (rs.next()) {
				servicePriority.put(rs.getByte("service_id"),
						rs.getString("priorityName"));
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
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			obj = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

	public static void setPath(String pathToSet) {
		path = pathToSet;
	}

	public static String getPath() {
		return path;
	}
}
