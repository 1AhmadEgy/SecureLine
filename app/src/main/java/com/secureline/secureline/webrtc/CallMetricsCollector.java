package com.secureline.secureline.webrtc;

public class CallMetricsCollector {

    private long totalBytesSent;
    private long totalBytesReceived;
    private int totalPacketsSent;
    private int totalPacketsReceived;
    private int totalPacketsLost;
    private long callDurationMs;
    private long callStartTime;

    public CallMetricsCollector() {
        reset();
    }

    public void startCall() {
        callStartTime = System.currentTimeMillis();
    }

    public void endCall() {
        callDurationMs = System.currentTimeMillis() - callStartTime;
    }

    public void recordBytesSent(long bytes) {
        totalBytesSent += bytes;
    }

    public void recordBytesReceived(long bytes) {
        totalBytesReceived += bytes;
    }

    public void recordPacketSent() {
        totalPacketsSent++;
    }

    public void recordPacketReceived() {
        totalPacketsReceived++;
    }

    public void recordPacketLost() {
        totalPacketsLost++;
    }

    public double getPacketLossRate() {
        int total = totalPacketsSent + totalPacketsReceived;
        if (total == 0) return 0;
        return (double) totalPacketsLost / total * 100.0;
    }

    public double getAverageBitrate() {
        if (callDurationMs == 0) return 0;
        long totalBytes = totalBytesSent + totalBytesReceived;
        return (totalBytes * 8.0) / (callDurationMs / 1000.0);
    }

    public long getTotalBytesSent() {
        return totalBytesSent;
    }

    public long getTotalBytesReceived() {
        return totalBytesReceived;
    }

    public long getCallDurationMs() {
        return callDurationMs;
    }

    public void reset() {
        totalBytesSent = 0;
        totalBytesReceived = 0;
        totalPacketsSent = 0;
        totalPacketsReceived = 0;
        totalPacketsLost = 0;
        callDurationMs = 0;
        callStartTime = 0;
    }
}
