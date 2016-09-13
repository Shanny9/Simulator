package utils;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import log.LogManager;
import log.Settings;

@WebListener
public class TimerManager implements ServletContextListener {

	private static ScheduledExecutorService scheduler;
	private static ClockIncrementor ci;
	private static LogManager lm;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		scheduler.shutdownNow();
	}

	public static HashMap<String, Object> getClocks() {
		return ClockIncrementor.getClocks();
	}

	public static void startSimulator(Settings settings, int round) {

		
		ci = ClockIncrementor.getInstance();
		ClockIncrementor.initialize(settings);

		lm = LogManager.getInstance();
		LogManager.initialize(settings);
		LogManager.setRound(round);
		
		runNTimes(ci, settings.getRoundTime() + 1, 0, 1, TimeUnit.SECONDS,
				scheduler);
		runNTimes(lm, settings.getRoundTime() + 1, 0, 1, TimeUnit.SECONDS,
				scheduler);
		System.out.println("TimerManager: simulator started.");
	}

	public static void forcePause() {
		if (ci == null || !ClockIncrementor.isRunning()) {
			return;
		}
		ClockIncrementor.forcePause();
	}

	public static void forceResume() {
		if (ci == null || ClockIncrementor.isRunning()) {
			return;
		}
		ClockIncrementor.forceResume();
	}

	/**
	 * Schedules a {@code Runnable} task of an {@code ScheduledExecutorService}
	 * to run N times at a fixed rate.
	 * 
	 * @param task
	 *            The task to execute
	 * @param maxRunCount
	 *            The number of iterations of the scheduled task
	 * 
	 * @param initDelay
	 *            the initial delay of the scheduled task.
	 * @param period
	 *            The amount of time of the between iterations of the task.
	 * @param unit
	 *            The time unit of period of time between iterations.
	 * @param executor
	 *            The scheduled executor service.
	 */
	private static void runNTimes(Runnable task, int maxRunCount,
			long initDelay, long period, TimeUnit unit,
			ScheduledExecutorService executor) {
		new FixedExecutionRunnable(task, maxRunCount).runNTimes(executor,
				initDelay, period, unit);
	}

}