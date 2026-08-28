package com.secureline.secureline.network;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeepAliveManager {

    private final ScheduledExecutorService scheduler;
    private final Runnable keepAliveTask;

    public KeepAliveManager(Runnable task) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.keepAliveTask = task;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(
            keepAliveTask,
            30,
            30,
            TimeUnit.SECONDS
        );
    }

    public void startWithInterval(int seconds) {
        scheduler.scheduleAtFixedRate(
            keepAliveTask,
            seconds,
            seconds,
            TimeUnit.SECONDS
        );
    }

    public void stop() {
        scheduler.shutdown();
    }
}
