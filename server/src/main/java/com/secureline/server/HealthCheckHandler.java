package com.secureline.server;

public class HealthCheckHandler {

    private final ServerLogger logger;

    public HealthCheckHandler() {
        this.logger = ServerLogger.getInstance();
    }

    public String checkHealth() {
        logger.info("Health check performed");
        return "{\"status\":\"healthy\",\"timestamp\":" + System.currentTimeMillis() + "}";
    }

    public boolean isServerHealthy() {
        return true;
    }

    public String getServerStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"status\": \"running\",\n");
        sb.append("  \"uptime\": \"").append(getUptime()).append("\",\n");
        sb.append("  \"memory\": \"").append(getMemoryUsage()).append("\"\n");
        sb.append("}");
        return sb.toString();
    }

    private String getUptime() {
        long uptime = System.currentTimeMillis() - ServerMain.getStartTime();
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        long max = runtime.maxMemory();
        return String.format("%.1fMB / %.1fMB", used / (1024.0 * 1024), max / (1024.0 * 1024));
    }
}
