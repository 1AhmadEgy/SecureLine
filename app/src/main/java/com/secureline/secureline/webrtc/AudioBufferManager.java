package com.secureline.secureline.webrtc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioBufferManager {

    private final Queue<byte[]> audioBuffer;
    private final int maxBufferSize;

    public AudioBufferManager(int maxSize) {
        audioBuffer = new ConcurrentLinkedQueue<>();
        this.maxBufferSize = maxSize;
    }

    public void addAudioData(byte[] data) {
        if (audioBuffer.size() >= maxBufferSize) {
            audioBuffer.poll();
        }
        audioBuffer.offer(data);
    }

    public byte[] getAudioData() {
        return audioBuffer.poll();
    }

    public byte[] peekAudioData() {
        return audioBuffer.peek();
    }

    public void clearBuffer() {
        audioBuffer.clear();
    }

    public int getBufferSize() {
        return audioBuffer.size();
    }

    public boolean isEmpty() {
        return audioBuffer.isEmpty();
    }

    public boolean isFull() {
        return audioBuffer.size() >= maxBufferSize;
    }
}
