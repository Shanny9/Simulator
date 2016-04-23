package utils;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import log.TeamLog;

@WebListener
public class TimerManager implements ServletContextListener {

	private static ScheduledExecutorService scheduler;
	private static ClockIncrementor ci;
 

	@Override
	public void contextInitialized(ServletContextEvent event) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		log.LogUtils.saveLog();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		scheduler.shutdownNow();
	}

	public static HashMap<String, Object> getClocks() {
		return ClockIncrementor.getClocks();
	}

	public static double getTeamProfits(String teamName) {
		return log.Log.getInstance().getTeam(teamName).getCurrentProfit();
	}

	public static void startSimulator(int runTime, int roundTime, int round, int pauseTime ,int sessionTime) {
		System.out.println("TimerManager: starting simulator");
		ci = new ClockIncrementor(runTime, roundTime, round, pauseTime, sessionTime);
		scheduler.scheduleAtFixedRate(ci, 0, 1, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(log.Log.getInstance(), 0, 1, TimeUnit.SECONDS);
	}

	public static void pauseSimulator() {
		System.err.println("TimerManager: pausing clock...");
		ClockIncrementor.pause();
		log.Log.pause();
		log.LogUtils.saveLog();
	}

	public static void resumeSimulator() {
		System.err.println("TimerManager: pausing clock...");
		ClockIncrementor.resume();
		log.Log.resume();
	}

}