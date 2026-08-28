package com.secureline.secureline.webrtc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioStreamManager {

    private final Queue<byte[]> incomingStream;
    private final Queue<byte[]> outgoingStream;
    private boolean streaming;
    private StreamListener listener;

    public AudioStreamManager() {
        incomingStream = new ConcurrentLinkedQueue<>();
        outgoingStream = new ConcurrentLinkedQueue<>();
        streaming = false;
    }

    public void startStreaming() {
        streaming = true;
    }

    public void stopStreaming() {
        streaming = false;
        incomingStream.clear();
        outgoingStream.clear();
    }

    public void addIncomingData(byte[] data) {
        if (streaming) {
            incomingStream.offer(data);
        }
    }

    public void addOutgoingData(byte[] data) {
        if (streaming) {
            outgoingStream.offer(data);
        }
    }

    public byte[] getNextIncomingData() {
        return incomingStream.poll();
    }

    public byte[] getNextOutgoingData() {
        return outgoingStream.poll();
    }

    public boolean isStreaming() {
        return streaming;
    }

    public int getIncomingQueueSize() {
        return incomingStream.size();
    }

    public int getOutgoingQueueSize() {
        return outgoingStream.size();
    }

    public void setListener(StreamListener listener) {
        this.listener = listener;
    }

    public interface StreamListener {
        void onDataReceived(byte[] data);
        void onDataSent(byte[] data);
    }
}
