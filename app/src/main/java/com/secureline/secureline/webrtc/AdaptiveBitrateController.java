package com.secureline.secureline.webrtc;

public class AdaptiveBitrateController {

    private int currentBitrate;
    private final int minBitrate;
    private final int maxBitrate;
    private final int stepSize;

    public AdaptiveBitrateController(int minBitrate, int maxBitrate) {
        this.minBitrate = minBitrate;
        this.maxBitrate = maxBitrate;
        this.stepSize = 4;
        this.currentBitrate = (minBitrate + maxBitrate) / 2;
    }

    public int getCurrentBitrate() {
        return currentBitrate;
    }

    public void increaseBitrate() {
        currentBitrate = Math.min(currentBitrate + stepSize, maxBitrate);
    }

    public void decreaseBitrate() {
        currentBitrate = Math.max(currentBitrate - stepSize, minBitrate);
    }

    public void updateBasedOnNetwork(NetworkConditionEstimator estimator) {
        if (estimator.isNetworkStable()) {
            increaseBitrate();
        } else {
            decreaseBitrate();
        }
    }

    public void setBitrate(int bitrate) {
        currentBitrate = Math.max(minBitrate, Math.min(bitrate, maxBitrate));
    }
}
