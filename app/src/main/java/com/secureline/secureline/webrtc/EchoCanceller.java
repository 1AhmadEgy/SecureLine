package com.secureline.secureline.webrtc;

public class EchoCanceller {

    private short[] referenceSignal;
    private boolean enabled;
    private int filterLength;

    public EchoCanceller(int filterLength) {
        this.filterLength = filterLength;
        this.referenceSignal = new short[filterLength];
        this.enabled = true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public short[] process(short[] input, short[] reference) {
        if (!enabled) return input;

        updateReference(reference);
        short[] output = new short[input.length];

        for (int i = 0; i < input.length; i++) {
            int echoEstimate = 0;
            for (int j = 0; j < filterLength; j++) {
                echoEstimate += referenceSignal[j] * 0.1;
            }
            output[i] = (short) (input[i] - echoEstimate);
        }

        return output;
    }

    private void updateReference(short[] reference) {
        if (reference == null || reference.length == 0) return;
        System.arraycopy(reference, 0, referenceSignal, 
            filterLength - reference.length, reference.length);
    }
}
