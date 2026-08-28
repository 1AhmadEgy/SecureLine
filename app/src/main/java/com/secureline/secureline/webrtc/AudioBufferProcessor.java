package com.secureline.secureline.webrtc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioBufferProcessor {

    private final Queue<short[]> audioBuffer;
    private final int maxBufferSize;
    private boolean processing;

    public AudioBufferProcessor(int maxSize) {
        this.audioBuffer = new ConcurrentLinkedQueue<>();
        this.maxBufferSize = maxSize;
        this.processing = false;
    }

    public void addAudioData(short[] data) {
        if (audioBuffer.size() >= maxBufferSize) {
            audioBuffer.poll();
        }
        audioBuffer.offer(data);
    }

    public short[] getNextAudioData() {
        return audioBuffer.poll();
    }

    public void startProcessing() {
        processing = true;
    }

    public void stopProcessing() {
        processing = false;
        audioBuffer.clear();
    }

    public boolean isProcessing() {
        return processing;
    }

    public int getBufferSize() {
        return audioBuffer.size();
    }

    public void clearBuffer() {
        audioBuffer.clear();
    }
}
