package utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class FixedExecutionRunnable implements Runnable {
	private final AtomicInteger runCount = new AtomicInteger();
	private final Runnable delegate;
	private volatile ScheduledFuture<?> self;
	private final int maxRunCount;

	public FixedExecutionRunnable(Runnable delegate, int maxRunCount) {
		this.delegate = delegate;
		this.maxRunCount = maxRunCount;
	}

	@Override
	public void run() {
		delegate.run();
		if (runCount.incrementAndGet() == maxRunCount) {
			boolean interrupted = false;
			try {
				while (self == null) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						interrupted = true;
					}
				}
				self.cancel(false);
			} finally {
				if (interrupted) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	/**
	 * Schedules a {@code Runnable} task of an {@code ScheduledExecutorService}
	 * to run at a fix rate.
	 * 
	 * @param executor
	 *            The scheduled executor service.
	 * @param initDelay
	 *            the initial delay of the scheduled task.
	 * @param period
	 *            The amount of time of the between iterations of the task.
	 * @param unit
	 *            The time unit of period of time between iterations.
	 */
	public void runNTimes(ScheduledExecutorService executor, long initDelay,
			long period, TimeUnit unit) {
		self = executor.scheduleAtFixedRate(this, initDelay, period, unit);
	}
}