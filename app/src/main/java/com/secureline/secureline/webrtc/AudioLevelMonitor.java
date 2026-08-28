package com.secureline.secureline.webrtc;

public class AudioLevelMonitor {

    private int currentLevel;
    private int peakLevel;
    private AudioLevelListener listener;

    public AudioLevelMonitor() {
        currentLevel = 0;
        peakLevel = 0;
    }

    public void updateAudioLevel(short[] audioSamples) {
        if (audioSamples == null || audioSamples.length == 0) return;

        int sum = 0;
        for (short sample : audioSamples) {
            sum += Math.abs(sample);
        }
        int average = sum / audioSamples.length;

        currentLevel = (int) ((average / 32768.0) * 100);
        if (currentLevel > peakLevel) {
            peakLevel = currentLevel;
        }

        if (listener != null) {
            listener.onAudioLevelChanged(currentLevel);
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getPeakLevel() {
        return peakLevel;
    }

    public void resetPeak() {
        peakLevel = 0;
    }

    public void setListener(AudioLevelListener listener) {
        this.listener = listener;
    }

    public interface AudioLevelListener {
        void onAudioLevelChanged(int level);
    }
}
