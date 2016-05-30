package utils;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounterListener implements HttpSessionListener {

	private static final AtomicInteger sessionCount = new AtomicInteger(0);

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		sessionCount.incrementAndGet();
		System.out.println(sessionCount.get());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		sessionCount.decrementAndGet();
		System.out.println(sessionCount.get());

	}

	public static int getSessionCount(String team) {
		return sessionCount.get();
		
	}
}