package com.secureline.secureline.security;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeyRotationScheduler {

    private final ScheduledExecutorService scheduler;
    private final Runnable rotationTask;

    public KeyRotationScheduler(Runnable task) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.rotationTask = task;
    }

    public void scheduleDailyRotation() {
        scheduler.scheduleAtFixedRate(
            rotationTask,
            24,
            24,
            TimeUnit.HOURS
        );
    }

    public void scheduleHourlyRotation() {
        scheduler.scheduleAtFixedRate(
            rotationTask,
            1,
            1,
            TimeUnit.HOURS
        );
    }

    public void stop() {
        scheduler.shutdown();
    }
}
