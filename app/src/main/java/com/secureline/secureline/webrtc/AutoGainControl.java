package com.secureline.secureline.webrtc;

public class AutoGainControl {

    private double currentGain;
    private final double targetLevel;
    private final double maxGain;
    private final double minGain;
    private final double adaptationRate;

    public AutoGainControl() {
        currentGain = 1.0;
        targetLevel = 0.3;
        maxGain = 10.0;
        minGain = 0.1;
        adaptationRate = 0.01;
    }

    public short[] process(short[] input) {
        double averageLevel = calculateAverageLevel(input);
        double desiredGain = targetLevel / (averageLevel + 1e-10);
        desiredGain = Math.max(minGain, Math.min(desiredGain, maxGain));
        currentGain = currentGain + adaptationRate * (desiredGain - currentGain);

        short[] output = new short[input.length];
        for (int i = 0; i < input.length; i++) {
            int sample = (int) (input[i] * currentGain);
            if (sample > Short.MAX_VALUE) sample = Short.MAX_VALUE;
            if (sample < Short.MIN_VALUE) sample = Short.MIN_VALUE;
            output[i] = (short) sample;
        }
        return output;
    }

    private double calculateAverageLevel(short[] samples) {
        if (samples == null || samples.length == 0) return 0;
        double sum = 0;
        for (short sample : samples) {
            sum += Math.abs(sample) / 32768.0;
        }
        return sum / samples.length;
    }

    public double getCurrentGain() {
        return currentGain;
    }
}
