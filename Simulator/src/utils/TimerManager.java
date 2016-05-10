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
import com.sun.org.apache.bcel.internal.generic.LUSHR;

import log.LogUpdater;
import log.SimulationLog;
import log.TeamLog;

@WebListener
public class TimerManager implements ServletContextListener {

	private static ScheduledExecutorService scheduler;
	private static ClockIncrementor ci;
	private static LogUpdater lu;
 

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

//	public static double getTeamProfits(String teamName) {
//		return log.SimulationLog.getInstance().getTeam(teamName).getProfit(ci.get);
//	}

	public static void startSimulator(int runTime, int roundTime, int round, int pauseTime ,int sessionTime) {
		System.out.println("TimerManager: starting simulator");
		ci = new ClockIncrementor(runTime, roundTime, round, pauseTime, sessionTime);
		lu = new LogUpdater();
		scheduler.scheduleAtFixedRate(ci, 0, 1, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(lu, 0, 1, TimeUnit.SECONDS);
	}

	public static void pauseSimulator() {
		System.err.println("TimerManager: pausing clock...");
		ClockIncrementor.pause();
		lu.pauseLog();
	}

	public static void resumeSimulator() {
		System.err.println("TimerManager: resuming clock...");
		ClockIncrementor.resume();
		lu.resumeLog();
	}

}