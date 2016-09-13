package log;

public class LogManager implements Runnable {
	private static boolean isRunning;
	private static int elapsed_time;
	private static LogManager instance;
	private static boolean isInitialized;

	/**
	 * Initializes the simulation log (can be activated only once)
	 * 
	 * @param setings
	 */
	public static void initialize(Settings setings) {
		if (isInitialized) {
			System.err.println("LogManager initialize method failed: LogManager is already running");
			return;
		}

		SimulationLog.getInstance().initialize(setings);
		isRunning = false;
		isInitialized = true;
	}

	/**
	 * Sets the round of the simulation log
	 * 
	 * @param setings
	 */
	public static void setRound(int round) {
		SimulationLog.getInstance().setRound(round);
	}

	/**
	 * Constructor
	 */
	private LogManager() {
		isInitialized = false;
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
	 * Indicates if the {@code SimulationLog} has started.
	 * @return true of the {@code SimulationLog} has started or false otherwise.
	 */
	public static boolean isInitialized(){
		return isInitialized;
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
	public static void pauseLog(int time, boolean isForced) {
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
	public static void stop(int time) {
		if (!isRunning) {
			return;
		}

		isRunning = false;
		SimulationLog.getInstance().stopLogs(time);
		System.out.println("LogManager: Log stopped");
		log.LogUtils.saveLog(SimulationLog.getInstance().getSettings()
				.getCourseName(), SimulationLog.getInstance().getRound());
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
			elapsed_time++;

			if (SimulationLog.getInstance().getIncidentTimes().containsKey(elapsed_time)) {
				byte inc_id = SimulationLog.getInstance().getIncidentTimes().get(elapsed_time);
				SimulationLog.getInstance().incidentStarted(inc_id, elapsed_time);
			}
			SimulationLog.getInstance().updateTeamProfits(elapsed_time);
		}

	}
}
