package com.secureline.secureline.webrtc;

public class CallStatisticsCollector {

    private long totalPacketsSent;
    private long totalPacketsReceived;
    private long totalBytesSent;
    private long totalBytesReceived;
    private long totalPacketsLost;
    private long callStartTime;
    private long callEndTime;

    public CallStatisticsCollector() {
        totalPacketsSent = 0;
        totalPacketsReceived = 0;
        totalBytesSent = 0;
        totalBytesReceived = 0;
        totalPacketsLost = 0;
    }

    public void startCall() {
        callStartTime = System.currentTimeMillis();
    }

    public void endCall() {
        callEndTime = System.currentTimeMillis();
    }

    public void recordPacketSent(int size) {
        totalPacketsSent++;
        totalBytesSent += size;
    }

    public void recordPacketReceived(int size) {
        totalPacketsReceived++;
        totalBytesReceived += size;
    }

    public void recordPacketLost() {
        totalPacketsLost++;
    }

    public double getPacketLossRate() {
        long total = totalPacketsSent + totalPacketsReceived;
        if (total == 0) return 0;
        return (double) totalPacketsLost / total * 100.0;
    }

    public long getTotalBytesSent() {
        return totalBytesSent;
    }

    public long getTotalBytesReceived() {
        return totalBytesReceived;
    }

    public long getCallDurationMillis() {
        return callEndTime - callStartTime;
    }
}
