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

@WebListener
public class TimerManager implements ServletContextListener {

	private static ScheduledExecutorService scheduler;
	private static ClockIncrementor ci;
	private static MoneyCalculator mc;

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
	
	public static HashMap<String, Object> getProfits() {
		return mc.getProfits();
	}

	public static void startSimulator(int runTime, int roundTime, int round) {
		System.out.println("TimerManager: starting simulator");
		ci = new ClockIncrementor(runTime, roundTime, round);
//		mc = new MoneyCalculator(0/*initProfit*/);
		scheduler.scheduleAtFixedRate(ci, 0, 1, TimeUnit.SECONDS);
//		scheduler.scheduleAtFixedRate(mc, 0, 1, TimeUnit.SECONDS);
	}

}