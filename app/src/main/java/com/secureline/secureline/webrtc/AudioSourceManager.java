package com.secureline.secureline.webrtc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioSourceManager {

    private final Queue<AudioFrame> frameQueue;
    private boolean isCapturing;
    private AudioFrameListener listener;

    public AudioSourceManager() {
        frameQueue = new ConcurrentLinkedQueue<>();
        isCapturing = false;
    }

    public void startCapture() {
        isCapturing = true;
    }

    public void stopCapture() {
        isCapturing = false;
        frameQueue.clear();
    }

    public void addFrame(AudioFrame frame) {
        if (isCapturing) {
            frameQueue.offer(frame);
        }
    }

    public AudioFrame getNextFrame() {
        return frameQueue.poll();
    }

    public boolean isCapturing() {
        return isCapturing;
    }

    public void setListener(AudioFrameListener listener) {
        this.listener = listener;
    }

    public interface AudioFrameListener {
        void onFrameCaptured(AudioFrame frame);
    }
}
