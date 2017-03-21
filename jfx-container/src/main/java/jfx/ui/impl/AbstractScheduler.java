package jfx.ui.impl;

import static util.Verify.argNotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

import util.Verify;

/**
 * Calls the injected {@link Runnable} periodically on the JFX application
 * thread (when active between {@link #start()} and {@link #stop()}.
 */
abstract class AbstractScheduler {

	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private final Runnable job;

	private boolean active;

	AbstractScheduler(Runnable job) {
		this.job = argNotNull(job);
	}

	void commission() {
		Verify.hasBeenProvided(job);
		executor.scheduleWithFixedDelay(new JobWrapper(), 500, 500, TimeUnit.MILLISECONDS);
	}

	void decommission() {
		executor.shutdown();
		try {
			executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
	}

	void start() {
		this.active = true;
	}

	void stop() {
		this.active = false;
	}

	private class JobWrapper implements Runnable {
		@Override
		public void run() {
			if (active) {
				Platform.runLater(job);
			}
		}
	}
}