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
	private static LogManager lu;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		log.LogUtils.saveLog("course1", 1);
//		new Thread(new SimulationTester()).start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		scheduler.shutdownNow();
	}

	public static HashMap<String, Object> getClocks() {
		return ClockIncrementor.getClocks();
	}

	public static void startSimulator(Settings settings) {
		System.out.println("TimerManager: starting simulator");
		ci = new ClockIncrementor(settings.getRunTime(), settings.getRoundTime(), settings.getLastRoundDone()+1, settings.getPauseTime());
		LogManager.setCourseName(settings.getCourseName());
		LogManager.setRound(settings.getLastRoundDone()+1);
		
//		scheduler.scheduleAtFixedRate(ci, 0, 1, TimeUnit.SECONDS);
//		scheduler.scheduleAtFixedRate(LogManager.getInstance(), 0, 1, TimeUnit.SECONDS);
		
		runNTimes(ci,settings.getRoundTime(),1,TimeUnit.SECONDS,scheduler);
		runNTimes(LogManager.getInstance(),settings.getRoundTime(),1,TimeUnit.SECONDS,scheduler);
		
	}

	public static void forcePause() {
		if (ci == null) {
			return;
		}
		System.err.println("TimerManager: pausing clock...");
		ClockIncrementor.forcePause();
	}

	public static void forceResume() {
		if (ci == null) {
			return;
		}
		System.err.println("TimerManager: resuming clock...");
		ClockIncrementor.forceResume();
	}
	
	public static void runNTimes(Runnable task, int maxRunCount, long period, TimeUnit unit, ScheduledExecutorService executor) {
	    new FixedExecutionRunnable(task, maxRunCount).runNTimes(executor, period, unit);
	}

}