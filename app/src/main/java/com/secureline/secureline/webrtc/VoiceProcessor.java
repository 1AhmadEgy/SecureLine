package com.secureline.secureline.webrtc;

public class VoiceProcessor {

    private boolean noiseSuppressionEnabled = true;
    private boolean echoCancellationEnabled = true;
    private boolean autoGainControlEnabled = true;
    private int noiseLevel = 2;

    public void enableNoiseSuppression(boolean enabled) {
        this.noiseSuppressionEnabled = enabled;
    }

    public void enableEchoCancellation(boolean enabled) {
        this.echoCancellationEnabled = enabled;
    }

    public void enableAutoGainControl(boolean enabled) {
        this.autoGainControlEnabled = enabled;
    }

    public void setNoiseLevel(int level) {
        this.noiseLevel = Math.max(1, Math.min(5, level));
    }

    public boolean isNoiseSuppressionEnabled() {
        return noiseSuppressionEnabled;
    }

    public boolean isEchoCancellationEnabled() {
        return echoCancellationEnabled;
    }

    public boolean isAutoGainControlEnabled() {
        return autoGainControlEnabled;
    }

    public int getNoiseLevel() {
        return noiseLevel;
    }
}
