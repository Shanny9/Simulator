package utils;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import log.LogManager;

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

	public static void startSimulator(String courseName, int runTime, int roundTime, int round, int pauseTime) {
		System.out.println("TimerManager: starting simulator");
		ci = new ClockIncrementor(runTime, roundTime, round, pauseTime, (runTime+pauseTime));
		LogManager.setCourseName(courseName);
		LogManager.setRound(round);
		scheduler.scheduleAtFixedRate(ci, 0, 1, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(LogManager.getInstance(), 0, 1, TimeUnit.SECONDS);
		
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

}