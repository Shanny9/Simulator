package log;

import java.util.HashSet;

import utils.SimulationTime;

public class LogManager implements Runnable {
	/**
	 * Indicates whether the simulation is running.
	 */
	private static boolean isRunning;
	/**
	 * The <b>run time</b> elapsed.
	 */
	private static SimulationTime elapsed_time;
	/**
	 * The instance of the {@code LogManager}.
	 */
	private static LogManager instance;

	/**
	 * Initializes the simulation log (can be activated only once).
	 * 
	 * @param setings
	 */
	public static void initialize(Settings setings) {
		SimulationLog.getInstance().initialize(setings);
		isRunning = false;
	}

	/**
	 * Sets the round of the simulation log
	 * 
	 * @param setings
	 */
	public static void setRound(int round) {
		SimulationLog.getInstance().setRound(round);
		elapsed_time = new SimulationTime(round, 1, 0);
	}

	/**
	 * Constructor
	 */
	private LogManager() {
	}

	/**
	 * Singleton
	 * 
	 * @return
	 */
	public static LogManager getInstance() {
		if (instance == null) {
			instance = new LogManager();
		}
		return instance;
	}

	/**
	 * Pauses the log. If the pause is not forced (run time ended), fixes all
	 * the teams' incidents.
	 * 
	 * @param time
	 *            The time of the pause
	 * @param isForced
	 *            True if was caused by the user. False if occurred after run
	 *            time.
	 */
	public static void pauseLog(SimulationTime time, boolean isForced) {
		isRunning = false;
		if (!isForced) {
			SimulationLog.getInstance().fixAllIncidents(time);
		}
	}

	/**
	 * Resumes the simulation log.
	 */
	public static void resumeLog() {
		isRunning = true;
	}

	/**
	 * Stops the simulation log. Puts end times to both teams' services.
	 * 
	 * @param time
	 *            The time of the stop
	 */
	public static void stop() {
		if (!isRunning) {
			return;
		}
		
		SimulationLog simLog = SimulationLog.getInstance();
		int round = simLog.getRound();
		Settings sett = simLog.getSettings();
		SimulationTime endTime = sett.getRoundRunTimeEnd(round);

		pauseLog(endTime, false);
		simLog.stopLogs(endTime);
		System.out.println("LogManager: Log stopped");
		FilesUtils.saveLog(sett.getCourseName(), round);
	}

	/**
	 * Calls the {@code incidentStarted} method of the {@code SimulationLog}
	 * when time is right and calls {@code updateTeamProfits} method every
	 * second.
	 */
	@Override
	public void run() {
		if (isRunning) {
			// occurs every second
			elapsed_time.increment();

			if (SimulationLog.getInstance().getIncidents()
					.containsKey(elapsed_time)) {
				HashSet<Byte> ci_ids = SimulationLog.getInstance()
						.getIncidents().get(elapsed_time);
				System.out
						.println(elapsed_time.toString() + ": cis: " + ci_ids);
				SimulationLog.getInstance().incidentsStarted(ci_ids,
						elapsed_time);
			}
			SimulationLog.getInstance().updateTeamProfits(elapsed_time);
		}
	}
}
