package com.secureline.secureline.webrtc;

public class AudioMixer {

    public static short[] mixAudio(short[] audio1, short[] audio2) {
        int length = Math.max(audio1.length, audio2.length);
        short[] mixed = new short[length];

        for (int i = 0; i < length; i++) {
            int sample1 = i < audio1.length ? audio1[i] : 0;
            int sample2 = i < audio2.length ? audio2[i] : 0;
            int sum = sample1 + sample2;
            if (sum > Short.MAX_VALUE) sum = Short.MAX_VALUE;
            if (sum < Short.MIN_VALUE) sum = Short.MIN_VALUE;
            mixed[i] = (short) sum;
        }
        return mixed;
    }

    public static short[] applyGain(short[] audio, float gain) {
        short[] result = new short[audio.length];
        for (int i = 0; i < audio.length; i++) {
            int sample = (int) (audio[i] * gain);
            if (sample > Short.MAX_VALUE) sample = Short.MAX_VALUE;
            if (sample < Short.MIN_VALUE) sample = Short.MIN_VALUE;
            result[i] = (short) sample;
        }
        return result;
    }
}
