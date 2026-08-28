package com.secureline.secureline.webrtc;

public class VoiceActivityDetector {

    private final int energyThreshold;
    private final int zeroCrossingThreshold;
    private boolean voiceDetected;

    public VoiceActivityDetector(int energyThreshold, int zeroCrossingThreshold) {
        this.energyThreshold = energyThreshold;
        this.zeroCrossingThreshold = zeroCrossingThreshold;
        this.voiceDetected = false;
    }

    public boolean detectVoice(short[] samples) {
        double energy = calculateEnergy(samples);
        int zeroCrossings = calculateZeroCrossings(samples);

        voiceDetected = energy > energyThreshold && zeroCrossings < zeroCrossingThreshold;
        return voiceDetected;
    }

    private double calculateEnergy(short[] samples) {
        double energy = 0;
        for (short sample : samples) {
            energy += (sample / 32768.0) * (sample / 32768.0);
        }
        return energy / samples.length;
    }

    private int calculateZeroCrossings(short[] samples) {
        int crossings = 0;
        for (int i = 1; i < samples.length; i++) {
            if ((samples[i] >= 0 && samples[i - 1] < 0) ||
                (samples[i] < 0 && samples[i - 1] >= 0)) {
                crossings++;
            }
        }
        return crossings;
    }

    public boolean isVoiceDetected() {
        return voiceDetected;
    }
}
