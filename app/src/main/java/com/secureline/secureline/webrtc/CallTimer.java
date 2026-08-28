package com.secureline.secureline.webrtc;

public class CallTimer {

    private long startTime;
    private long accumulatedTime;
    private boolean isRunning;

    public CallTimer() {
        startTime = 0;
        accumulatedTime = 0;
        isRunning = false;
    }

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
        }
    }

    public void pause() {
        if (isRunning) {
            accumulatedTime += System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    public void resume() {
        start();
    }

    public void reset() {
        startTime = 0;
        accumulatedTime = 0;
        isRunning = false;
    }

    public long getElapsedTime() {
        if (isRunning) {
            return accumulatedTime + (System.currentTimeMillis() - startTime);
        }
        return accumulatedTime;
    }

    public String getFormattedTime() {
        long totalSeconds = getElapsedTime() / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
