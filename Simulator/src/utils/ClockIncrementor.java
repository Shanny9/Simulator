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

	private static int current_round;

	private static int current_session;

	/**
	 * Initializes the {@code ClockIncrementor}
	 * 
	 * @param settings
	 *            The course's settings
	 */
	public static void initialize(Settings settings, int current_round) {
		ClockIncrementor.current_round = current_round;
		ClockIncrementor.current_session = 1;
		ClockIncrementor.settings = settings;
		initVariables();

		System.out
				.println("ClockIncrementor: ClockIncrementor is initialized.");
	}

	/**
	 * Local constructor.
	 */
	private ClockIncrementor() {
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
	 * @param currentRound
	 *            The current round
	 * @return A {@code HashMap} of the following: <li>{@code elapsedClock (int)}
	 *         </li> <li>{@code remainingClock (int)}</li> <li>
	 *         {@code elapsedRunTime (SimulationTime)}</li> <li>
	 *         {@code isRunTime (boolean)}</li> <li>
	 *         {@code round (int)}</li> <li>
	 *         {@code session (boolean)}</li>
	 */
	public static HashMap<String, Object> getClocks() {

		HashMap<String, Object> clocks = new HashMap<>();
		clocks.put("elapsedClock",
				elapsedTime + (current_round - 1) * settings.getRoundTime());
		clocks.put("remainingClock", remainingTime);
		clocks.put("elapsedRunTime", elapsedRunTime.getRunTime()
				+ (current_round - 1) * settings.getRoundRunTime());
		clocks.put("isRunTime", isRunTime);
		clocks.put("round", current_round);
		clocks.put("session", current_session);
		return clocks;
	}

	public void run() {
		isRunning = true;
		elapsedTime += 1;
		remainingTime -= 1;
		// System.out.println("ClockIncrementor: elapsed_time= " + elapsedTime);
		if (isRunTime) {
			elapsedRunTime.increment();
		}

		if ((elapsedTime + settings.getRunTime()) % settings.getSessionTime() == 0) {
			// finished pause time
			remainingTime = settings.getRunTime();
			isRunTime = true;
			LogManager.resumeLog();

		} else if (elapsedTime % settings.getSessionTime() == 0) {
			// finished run time
			current_session++;
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
	}

	/**
	 * Initializes the variables of the {@code ClockIncrementor}. This method
	 * should be called before each run.
	 */
	private static void initVariables() {
		elapsedTime = 0;
		elapsedRunTime = new SimulationTime(0);
		remainingTime = settings.getPauseTime();
		isRunTime = false;
	}

	/**
	 * Returns the simulation's run time.
	 * 
	 * @return The simulation run time.
	 */
	public static SimulationTime getSimRunTime() {
		int runTime = elapsedRunTime.getRunTime() + (current_round - 1)
				* settings.getRoundRunTime();
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

	public static boolean isPauseTime() {
		return !isRunTime;
	}

	public static boolean isRunTime() {
		return isRunTime;
	}
}
