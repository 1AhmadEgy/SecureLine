package com.secureline.secureline.webrtc;

public class CallQualityManager {

    public enum QualityMode {
        HIGH_QUALITY,
        DATA_SAVER,
        EXTREME_SAVER
    }

    private QualityMode currentMode = QualityMode.DATA_SAVER;

    public CallQualityManager(QualityMode mode) {
        this.currentMode = mode;
    }

    public String getOpusBitrate() {
        switch (currentMode) {
            case HIGH_QUALITY:
                return "32";
            case DATA_SAVER:
                return "16";
            case EXTREME_SAVER:
                return "8";
            default:
                return "16";
        }
    }

    public boolean useForwardErrorCorrection() {
        return true;
    }

    public boolean useEchoCancellation() {
        return true;
    }
}
