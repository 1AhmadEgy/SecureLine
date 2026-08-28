package com.secureline.secureline.network;

import com.secureline.secureline.crypto.ObfuscationLayer;
import com.secureline.secureline.crypto.SignalProtocolManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageSender {

    private final SignalProtocolManager protocolManager;
    private final ExecutorService executorService;

    public MessageSender(SignalProtocolManager manager) {
        this.protocolManager = manager;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void sendMessage(String remoteAddress, byte[] plaintext, SendCallback callback) {
        executorService.execute(() -> {
            byte[] encrypted = protocolManager.encryptMessage(remoteAddress, plaintext);
            if (encrypted == null) {
                callback.onFailure("Encryption failed");
                return;
            }
            byte[] obfuscated = ObfuscationLayer.obfuscate(encrypted);
            boolean success = sendToServer(remoteAddress, obfuscated);
            if (success) {
                callback.onSuccess();
            } else {
                callback.onFailure("Network error");
            }
        });
    }

    private boolean sendToServer(String remoteAddress, byte[] data) {
        // Implement network sending
        return true;
    }

    public interface SendCallback {
        void onSuccess();
        void onFailure(String error);
    }
}
