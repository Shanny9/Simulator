package log;

public class LogUpdater implements Runnable {
	private SimulationLog simLog;
	private boolean threadRunning; 
	private int elapsed_time;

	public LogUpdater() {
		super();
		simLog = SimulationLog.getInstance();
		threadRunning = true;
	}

	public void pauseLog() {
		threadRunning = false;
		log.LogUtils.saveLog();
		System.out.println("Log paused");
	}

	public void resumeLog() {
		threadRunning = true;
		System.out.println("Log resumed");
	}

	public void Stop(int time) {
		pauseLog();
		simLog.getTeam("Marom").Stop(time);
		simLog.getTeam("Rakia").Stop(time);
		System.out.println("Log stopped");
	}

	@Override
	public void run() {
		if (threadRunning) {
			TeamLog marom = simLog.getTeam("Marom");
			TeamLog rakia = simLog.getTeam("Rakia");
			// should occur every second
			elapsed_time++;
			if (simLog.getIncidentTimes().containsKey(elapsed_time)) {
				int inc_id = simLog.getIncidentTimes().get(elapsed_time);
				marom.incidentStarted(inc_id, elapsed_time);
				rakia.incidentStarted(inc_id, elapsed_time);
			}
			marom.updateProfit(elapsed_time);
			rakia.updateProfit(elapsed_time);
			
/*			System.out.println("Marom: " + marom.getProfits());
			System.out.println("Rakia: " + rakia.getProfits());
			System.out.println("");*/
		}
	}
}
