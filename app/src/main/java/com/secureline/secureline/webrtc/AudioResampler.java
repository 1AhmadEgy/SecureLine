package com.secureline.secureline.webrtc;

public class AudioResampler {

    public static short[] resample(short[] input, int inputRate, int outputRate) {
        if (inputRate == outputRate) return input.clone();

        double ratio = (double) outputRate / inputRate;
        int outputLength = (int) (input.length * ratio);
        short[] output = new short[outputLength];

        for (int i = 0; i < outputLength; i++) {
            double inputIndex = i / ratio;
            int index0 = (int) inputIndex;
            int index1 = Math.min(index0 + 1, input.length - 1);
            double fraction = inputIndex - index0;
            output[i] = (short) (input[index0] * (1 - fraction) + input[index1] * fraction);
        }
        return output;
    }
}
