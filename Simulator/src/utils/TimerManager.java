package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
		PrintStream out;
		try {
		out = new PrintStream(new FileOutputStream("C:\\Users\\Shanny9\\Desktop\\output.txt"));
		System.setOut(out);
		System.out.println("H");

		} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		
		System.out.println("TimerManager: starting simulator");
		ci = new ClockIncrementor(settings, round);
		lm = LogManager.getInstance(settings.getCourseName());
		lm.setRound(round);
		
		runNTimes(ci,settings.getRoundTime(), 0, 1,TimeUnit.SECONDS,scheduler);
		runNTimes(lm,settings.getRoundTime(),0, 1,TimeUnit.SECONDS,scheduler);
		
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
	
	public static void runNTimes(Runnable task, int maxRunCount, long initDelay, long period, TimeUnit unit, ScheduledExecutorService executor) {
	    new FixedExecutionRunnable(task, maxRunCount).runNTimes(executor, initDelay, period, unit);
	}

}