package com.secureline.secureline.network;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerHealthCheck {

    private final ScheduledExecutorService scheduler;
    private final String healthUrl;
    private HealthListener listener;

    public ServerHealthCheck(String url) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.healthUrl = url;
    }

    public void startMonitoring() {
        scheduler.scheduleAtFixedRate(this::checkHealth, 0, 30, TimeUnit.SECONDS);
    }

    public void stopMonitoring() {
        scheduler.shutdown();
    }

    private void checkHealth() {
        boolean isHealthy = performHealthCheck();
        if (listener != null) {
            if (isHealthy) {
                listener.onServerHealthy();
            } else {
                listener.onServerUnhealthy();
            }
        }
    }

    private boolean performHealthCheck() {
        try {
            URL url = new URL(healthUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public void setListener(HealthListener listener) {
        this.listener = listener;
    }

    public interface HealthListener {
        void onServerHealthy();
        void onServerUnhealthy();
    }
}
