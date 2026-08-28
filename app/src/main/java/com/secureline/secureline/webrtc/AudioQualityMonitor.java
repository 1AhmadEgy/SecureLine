package com.secureline.secureline.webrtc;

import java.util.ArrayList;
import java.util.List;

public class AudioQualityMonitor {

    private final List<RTCPFeedback> feedbackHistory;
    private static final int MAX_HISTORY = 100;

    public AudioQualityMonitor() {
        feedbackHistory = new ArrayList<>();
    }

    public void addFeedback(RTCPFeedback feedback) {
        feedbackHistory.add(feedback);
        if (feedbackHistory.size() > MAX_HISTORY) {
            feedbackHistory.remove(0);
        }
    }

    public double getAveragePacketLoss() {
        if (feedbackHistory.isEmpty()) return 0;
        double total = 0;
        for (RTCPFeedback feedback : feedbackHistory) {
            total += feedback.getPacketLossPercentage();
        }
        return total / feedbackHistory.size();
    }

    public double getAverageJitter() {
        if (feedbackHistory.isEmpty()) return 0;
        double total = 0;
        for (RTCPFeedback feedback : feedbackHistory) {
            total += feedback.getJitter();
        }
        return total / feedbackHistory.size();
    }

    public double getAverageRoundTripTime() {
        if (feedbackHistory.isEmpty()) return 0;
        double total = 0;
        for (RTCPFeedback feedback : feedbackHistory) {
            total += feedback.getRoundTripTime();
        }
        return total / feedbackHistory.size();
    }

    public boolean shouldReduceBitrate() {
        return getAveragePacketLoss() > 10 || 
               getAverageJitter() > 40 || 
               getAverageRoundTripTime() > 400;
    }

    public boolean shouldIncreaseBitrate() {
        return getAveragePacketLoss() < 2 && 
               getAverageJitter() < 20 && 
               getAverageRoundTripTime() < 200;
    }
}
