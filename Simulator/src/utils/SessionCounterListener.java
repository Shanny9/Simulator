package utils;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounterListener implements HttpSessionListener {

	private static final AtomicInteger maromCount = new AtomicInteger(0);
	private static final AtomicInteger rakiaCount = new AtomicInteger(0);

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		updateCount(event,true);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		sessionCount.decrementAndGet();
		System.out.println(sessionCount.get());

	public static int getSessionCount(String team) {
		if (team.equalsIgnoreCase("marom")){
			return maromCount.get();
		} else if (team.equalsIgnoreCase("rakia")){
			return rakiaCount.get();
		}
		return 0;
	}

	private void updateCount(HttpSessionEvent event, boolean increment) {
		Object obj = event.getSession().getAttribute("team");
		String team;
		if (obj instanceof String) {
			team = obj.toString();

			if (team.equalsIgnoreCase("marom")) {
				if (increment) {
					maromCount.incrementAndGet();
				} else {
					maromCount.decrementAndGet();
				}
			} else if (team.equalsIgnoreCase("rakia")) {
				if (increment) {
					rakiaCount.incrementAndGet();
				} else{
					rakiaCount.decrementAndGet();
				}
			}
		}
	}

}