package com.secureline.secureline.webrtc;

import com.secureline.secureline.crypto.ObfuscationLayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EncryptedAudioStream {

    private final BlockingQueue<byte[]> audioQueue;
    private boolean running = false;
    private Thread streamThread;

    public EncryptedAudioStream() {
        audioQueue = new LinkedBlockingQueue<>(100);
    }

    public void start() {
        if (running) return;
        running = true;
        streamThread = new Thread(() -> {
            while (running) {
                try {
                    byte[] audioData = audioQueue.take();
                    byte[] obfuscated = ObfuscationLayer.obfuscate(audioData);
                    sendToNetwork(obfuscated);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        streamThread.start();
    }

    public void stop() {
        running = false;
        if (streamThread != null) {
            streamThread.interrupt();
        }
        audioQueue.clear();
    }

    public void addAudioData(byte[] data) {
        if (running) {
            audioQueue.offer(data);
        }
    }

    private void sendToNetwork(byte[] data) {
        // Implement network sending logic
    }
}
