package com.secureline.secureline.webrtc;

public class RTCPFeedback {

    private int packetsSent;
    private int packetsReceived;
    private int packetsLost;
    private double jitter;
    private double roundTripTime;

    public RTCPFeedback() {
        packetsSent = 0;
        packetsReceived = 0;
        packetsLost = 0;
        jitter = 0;
        roundTripTime = 0;
    }

    public void updateStats(int sent, int received, int lost, double jitterMs, double rttMs) {
        this.packetsSent = sent;
        this.packetsReceived = received;
        this.packetsLost = lost;
        this.jitter = jitterMs;
        this.roundTripTime = rttMs;
    }

    public double getPacketLossPercentage() {
        int total = packetsSent + packetsLost;
        if (total == 0) return 0;
        return (double) packetsLost / total * 100.0;
    }

    public double getJitter() {
        return jitter;
    }

    public double getRoundTripTime() {
        return roundTripTime;
    }

    public boolean isConnectionQualityGood() {
        return getPacketLossPercentage() < 5 && jitter < 30 && roundTripTime < 300;
    }

    public boolean isConnectionQualityPoor() {
        return getPacketLossPercentage() > 15 || jitter > 50 || roundTripTime > 500;
    }
}
