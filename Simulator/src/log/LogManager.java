package log;

public class LogManager implements Runnable {
	private static SimulationLog simLog;
	private static boolean isRunning; 
	private static int elapsed_time;
	private static String course;
	private static int round;
	private static LogManager instance;

	LogManager() {
		super();
		simLog = SimulationLog.getInstance();
		isRunning = true;
	}
	
	public static LogManager getInstance(){
		if (instance == null){
			instance = new LogManager();
		}
		return instance;
	}
	
	public static void setCourseName(String currentCourse){
		course = currentCourse;
		simLog.setCourseName(currentCourse);
	}
	
	public static void setRound(int currentRound){
		round = currentRound;
	}

	/**
	 * Pauses the log. If the pause is not forced (run time ended), fixes all the teams' incidents.
	 * 
	 * @param time The time of the pause
	 * @param isForced True if was caused by the user. False if occurred after run time.
	 */
	public static void pauseLog(int time, boolean isForced) {
		isRunning = false;
		if (!isForced){
			simLog.fixAllIncidents(time);
		}
		System.out.println("Log paused");
	}

	/**
	 * Resumes the log.
	 */
	public static void resumeLog() {
		isRunning = true;
		System.out.println("Log resumed");
	}

	/**
	 * Stops the log. Puts end times to both teams' services.
	 * 
	 * @param time The time of the stop
	 */
	public static void Stop(int time) {
		isRunning = false;
		simLog.stopLogs(time);
		System.out.println("Log stopped");
		log.LogUtils.saveLog(course,round);
	}

	@Override
	public void run() {
//		long start = System.nanoTime();
		if (isRunning) {
			// should occur every second
			elapsed_time++;

			if (simLog.getIncidentTimes().containsKey(elapsed_time)) {
				int inc_id = simLog.getIncidentTimes().get(elapsed_time);
				simLog.incidentStarted(inc_id, elapsed_time);
			}
			simLog.updateTeamProfits(elapsed_time);
			
//			System.out.println("Marom: " + marom.getProfits());
//			System.out.println("Rakia: " + rakia.getProfits());
//			System.out.println("");
		}
//		long end = System.nanoTime();
//		System.out.println(end-start);
	}
}
