package utils;

import java.util.HashMap;

import log.LogManager;
import log.Settings;

public class ClockIncrementor implements Runnable {
	/**
	 * Indicates whether the {@code ClockIncrementor} is running.
	 */
	private static volatile boolean isRunning = false;
	/**
	 * The course's settings.
	 */
	private static Settings settings;
	/**
	 * The total elapsed time of the simulation (including pause times).
	 */
	private static int elapsedTime;
	/**
	 * The total remaining time until the end of current phase (run/pause time).
	 */
	private static int remainingTime;
	/**
	 * The elapsed run time of the simulation.
	 */
	private static SimulationTime elapsedRunTime;
	/**
	 * Indicates whether the simulation is currently in run time or pause time.
	 */
	private static boolean isRunTime;
	/**
	 * The instance of the {@code ClockIncrementor} class.
	 */
	private static ClockIncrementor instance;
	/**
	 * Indicates whether the {@code ClockIncrementor} is initialized or not.
	 */
	private static boolean isInitialized;

	/**
	 * Initializes the {@code ClockIncrementor}
	 * 
	 * @param _settings
	 *            The course's settings
	 */
	public static void initialize(Settings _settings) {

		if (isInitialized) {
			System.err
					.println("ClockIncrementor initialize method failed: ClockIncrementor is already initialized.");
			return;
		}

		settings = _settings;
		initVariables();

		isInitialized = true;
		System.out
				.println("ClockIncrementor: ClockIncrementor is initialized.");
	}

	/**
	 * Local constructor.
	 */
	private ClockIncrementor() {
		isInitialized = false;
	};

	/**
	 * Singleton method.
	 * 
	 * @return An instance of {@code ClockIncrementor}.
	 */
	public static ClockIncrementor getInstance() {
		if (instance == null) {
			instance = new ClockIncrementor();
		}
		return instance;
	}

	/**
	 * @param currentRound The current round
	 * @return A {@code HashMap} of the following: <li>{@code elapsedClock (int)}
	 *         </li> <li>{@code remainingClock (int)}</li> <li>
	 *         {@code elapsedRunTime (SimulationTime)}</li> <li>
	 *         {@code isRunTime (boolean)}</li>
	 */
	public static HashMap<String, Object> getClocks(int currentRound) {

		if (currentRound <= 0) {
			return null;
		}

		HashMap<String, Object> clocks = new HashMap<>();
		clocks.put("elapsedClock",
				elapsedTime + (currentRound - 1) * settings.getRoundTime());
		clocks.put("remainingClock", remainingTime);
		clocks.put("elapsedRunTime", elapsedRunTime.getRunTime()
				+ (currentRound - 1) * settings.getRoundRunTime());
		clocks.put("isRunTime", isRunTime);
		return clocks;
	}

	public void run() {
		if (isRunning) {
			elapsedTime += 1;
			remainingTime -= 1;

			if (isRunTime) {
				elapsedRunTime.increment();
			}

			if ((elapsedTime + settings.getRunTime())
					% settings.getSessionTime() == 0) {
				// finished pause time
				remainingTime = settings.getRunTime();
				isRunTime = true;
				LogManager.resumeLog();

			} else if (elapsedTime % settings.getSessionTime() == 0) {
				// finished run time

				if (elapsedTime % settings.getRoundTime() == 0) {
					// finished round
					isRunning = false;
					LogManager.stop();
					initVariables();
					System.out.println("ClockIncrementor: Round is finished.");
					return;

				} else {
					// starts pause time
					remainingTime = settings.getPauseTime();
					isRunTime = false;
					LogManager.pauseLog(elapsedRunTime, false);
				}
			}
		} else {
			return;
		}
	}

	/**
	 * Initializes the variables of the {@code ClockIncrementor}. This method
	 * should be called before each run.
	 */
	private static void initVariables() {
		elapsedTime = 0;
		elapsedRunTime = new SimulationTime(0);
		remainingTime = settings.getPauseTime();
		isRunning = true;
		isRunTime = false;
	}

	/**
	 * Returns the simulation's run time.
	 * 
	 * @return The simulation run time.
	 */
	public static SimulationTime getSimTime() {
		int runTime = elapsedRunTime.getRunTime();
		return new SimulationTime(runTime);
	}

	/**
	 * Forces the {@code ClockIncrementor} to stop.
	 */
	public static void forcePause() {
		isRunning = false;
		LogManager.pauseLog(elapsedRunTime, true);
		System.out.println("ClockIncrementor: Paused (elapsed time is "
				+ elapsedTime + " sec.)");
	}

	/**
	 * Forces the {@code ClockIncrementor} to resume.
	 */
	public static void forceResume() {
		isRunning = true;
		LogManager.resumeLog();
		System.out.println("ClockIncrementor: Resumed.");
	}

	/**
	 * Indicates if the {@code ClockIncrementor} is running.
	 * 
	 * @return {@code true} if the {@code ClockIncrementor} is running.
	 *         Otherwise returns {@code false}.
	 */
	public static boolean isRunning() {
		return isRunning;
	}
}
