package com.secureline.secureline.webrtc;

public class NetworkConditionEstimator {

    private double estimatedBandwidth;
    private double estimatedPacketLoss;
    private double estimatedLatency;

    public NetworkConditionEstimator() {
        estimatedBandwidth = 1000;
        estimatedPacketLoss = 0;
        estimatedLatency = 50;
    }

    public void updateEstimate(double bandwidth, double packetLoss, double latency) {
        this.estimatedBandwidth = bandwidth;
        this.estimatedPacketLoss = packetLoss;
        this.estimatedLatency = latency;
    }

    public double getEstimatedBandwidth() {
        return estimatedBandwidth;
    }

    public double getEstimatedPacketLoss() {
        return estimatedPacketLoss;
    }

    public double getEstimatedLatency() {
        return estimatedLatency;
    }

    public int recommendBitrate() {
        if (estimatedPacketLoss > 10) return 8;
        if (estimatedPacketLoss > 5) return 16;
        if (estimatedPacketLoss > 2) return 24;
        if (estimatedBandwidth > 1000) return 32;
        if (estimatedBandwidth > 500) return 24;
        return 16;
    }

    public boolean isNetworkStable() {
        return estimatedPacketLoss < 2 && estimatedLatency < 100;
    }
}
