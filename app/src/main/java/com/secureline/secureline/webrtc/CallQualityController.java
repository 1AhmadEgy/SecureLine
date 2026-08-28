package com.secureline.secureline.webrtc;

public class CallQualityController {

    private int currentBitrate;
    private int currentPacketLoss;
    private int currentJitter;
    private QualityLevel qualityLevel;

    public enum QualityLevel {
        EXCELLENT,
        GOOD,
        FAIR,
        POOR,
        CRITICAL
    }

    public CallQualityController() {
        currentBitrate = 16;
        currentPacketLoss = 0;
        currentJitter = 0;
        qualityLevel = QualityLevel.GOOD;
    }

    public void updateMetrics(int bitrate, int packetLoss, int jitter) {
        this.currentBitrate = bitrate;
        this.currentPacketLoss = packetLoss;
        this.currentJitter = jitter;
        updateQualityLevel();
    }

    private void updateQualityLevel() {
        if (currentPacketLoss < 1 && currentJitter < 20) {
            qualityLevel = QualityLevel.EXCELLENT;
        } else if (currentPacketLoss < 3 && currentJitter < 40) {
            qualityLevel = QualityLevel.GOOD;
        } else if (currentPacketLoss < 7 && currentJitter < 60) {
            qualityLevel = QualityLevel.FAIR;
        } else if (currentPacketLoss < 15 && currentJitter < 100) {
            qualityLevel = QualityLevel.POOR;
        } else {
            qualityLevel = QualityLevel.CRITICAL;
        }
    }

    public int getRecommendedBitrate() {
        switch (qualityLevel) {
            case EXCELLENT: return 32;
            case GOOD: return 24;
            case FAIR: return 16;
            case POOR: return 8;
            case CRITICAL: return 8;
            default: return 16;
        }
    }

    public QualityLevel getQualityLevel() {
        return qualityLevel;
    }

    public int getCurrentBitrate() {
        return currentBitrate;
    }
}
