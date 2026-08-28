package com.secureline.secureline.webrtc;

public class NoiseGate {

    private int threshold;
    private boolean gateOpen;

    public NoiseGate(int threshold) {
        this.threshold = threshold;
        this.gateOpen = false;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean processSample(short sample) {
        int amplitude = Math.abs(sample);
        if (amplitude > threshold) {
            gateOpen = true;
        } else if (amplitude < threshold / 2) {
            gateOpen = false;
        }
        return gateOpen;
    }

    public boolean isGateOpen() {
        return gateOpen;
    }
}
