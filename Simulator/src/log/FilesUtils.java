package log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import utils.SimulationTime;

public class FilesUtils {
	private static String path = "";
	private static final String date_time_foramt = "dd.MM.yy HH.mm";
	private static final String filePrefix = "round#";
	// TODO: ask user if he wants to override existing logs
	private static final boolean deleteHistory = true;

	/**
	 * Deletes The course's file directory.
	 * 
	 * @param courseName
	 *            The name of the course to delete.
	 */
	public static void deleteCourse(String courseName) {

		File file = new File(getPath() + File.separator + courseName);
		if (!file.exists()) {
			return;
		}

		try {
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return The names of all the courses.
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
	 * Saves the log in the logs folder.
	 * 
	 * @param courseName
	 *            The name of the course.
	 * @param round
	 *            The round of the simulation.
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
			simLog.getSettings().addRoundDone(round);
			saveSettings(sett);
			out.writeObject(simLog);
			out.close();
			fileOut.close();
			System.out.println("LogUtils: Log is saved");

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
	 * Fetches the log from the logs folder.
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

			in.close();
			fileIn.close();
			return log;
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("LogUtils: Log class not found");
			c.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates new directory for the course and saves a file named
	 * "<b>settings.ser</b>" in it
	 * 
	 * @param settings
	 *            The course's settings
	 */
	public static void saveSettings(Settings settings) {
		
		if (settings == null){
			return;
		}
		
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
		System.out.println("LogUtils: settings for course "
				+ settings.getCourseName() + " were saved in " + getPath());
	}

	/**
	 * Retrieves the course's settings
	 * 
	 * @param courseName
	 *            The name of the course to retrieve.
	 * @return The course's settings.
	 */
	public static Settings openSettings(String courseName) {
		Settings settings = null;
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
				settings = (Settings) in.readObject();
				in.close();
				fileIn.close();

			} else if (fileName.endsWith(".txt")) {
				fullPath = fullPath.replace("settings.ser", "settings.txt");
				try (BufferedReader buffer = new BufferedReader(new FileReader(
						fullPath))) {
					String line;
					String input = "";
					while ((line = buffer.readLine()) != null) {
						input += line;
					}

					settings = Settings.extractFromText(input);
					saveSettings(settings);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException | ClassNotFoundException i) {
			// TODO: tell the user reading the file has failed. May occur if the
			// Settings class has changed.
			i.printStackTrace();
		}
		if (settings != null){
			SimulationTime.initialize(settings.getRunTime(),
					settings.getPauseTime(), settings.getSessionsPerRound(),
					settings.getRounds());
		}
		return settings;
	}

	/**
	 * Generates a file name given the simulation's round.
	 * 
	 * @param round
	 *            The simulation's round.
	 * @return The file name of the requested log.
	 */
	private static String generateFileName(int round) {
		String dateString = new SimpleDateFormat(date_time_foramt)
				.format(new Date());
		return filePrefix + round + " - " + dateString + ".ser";
	}

	/**
	 * Sets the path in which all courses are saved.
	 * 
	 * @param pathToSet
	 *            The path to set.
	 */
	public static void setPath(String pathToSet) {
		path = pathToSet;
	}

	/**
	 * @return The path in which all courses are saved.
	 */
	public static String getPath() {
		return path;
	}
}
