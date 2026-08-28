package com.secureline.secureline.network;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReconnectionManager {

    private final ScheduledExecutorService scheduler;
    private final Runnable reconnectTask;
    private int attemptCount;
    private boolean isRunning;

    public ReconnectionManager(Runnable task) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.reconnectTask = task;
        this.attemptCount = 0;
        this.isRunning = false;
    }

    public void start() {
        if (isRunning) return;
        isRunning = true;
        attemptCount = 0;
        scheduleReconnect();
    }

    public void stop() {
        isRunning = false;
        attemptCount = 0;
        scheduler.shutdownNow();
    }

    private void scheduleReconnect() {
        if (!isRunning) return;

        long delay = calculateBackoff();
        scheduler.schedule(() -> {
            reconnectTask.run();
            attemptCount++;
            scheduleReconnect();
        }, delay, TimeUnit.MILLISECONDS);
    }

    private long calculateBackoff() {
        long base = 1000;
        long max = 60000;
        long backoff = base * (long) Math.pow(2, attemptCount);
        return Math.min(backoff, max);
    }

    public int getAttemptCount() {
        return attemptCount;
    }
}
