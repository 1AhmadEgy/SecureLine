package com.secureline.secureline.network;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionKeepAlive {

    private final ScheduledExecutorService scheduler;
    private final Runnable keepAliveTask;
    private boolean isRunning;

    public ConnectionKeepAlive(Runnable task) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.keepAliveTask = task;
        this.isRunning = false;
    }

    public void start(int intervalSeconds) {
        if (isRunning) return;
        isRunning = true;
        scheduler.scheduleAtFixedRate(
            keepAliveTask,
            intervalSeconds,
            intervalSeconds,
            TimeUnit.SECONDS
        );
    }

    public void stop() {
        isRunning = false;
        scheduler.shutdown();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
