package com.secureline.secureline.network;

import com.secureline.secureline.crypto.ObfuscationLayer;
import com.secureline.secureline.crypto.SignalProtocolManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageReceiver {

    private final SignalProtocolManager protocolManager;
    private final ExecutorService executorService;

    public MessageReceiver(SignalProtocolManager manager) {
        this.protocolManager = manager;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void receiveMessage(String remoteAddress, byte[] obfuscatedData, ReceiveCallback callback) {
        executorService.execute(() -> {
            byte[] encrypted = ObfuscationLayer.deobfuscate(obfuscatedData);
            if (encrypted == null || encrypted.length == 0) {
                callback.onFailure("Deobfuscation failed");
                return;
            }
            byte[] plaintext = protocolManager.decryptMessage(remoteAddress, encrypted);
            if (plaintext == null) {
                callback.onFailure("Decryption failed");
                return;
            }
            callback.onSuccess(plaintext);
        });
    }

    public interface ReceiveCallback {
        void onSuccess(byte[] plaintext);
        void onFailure(String error);
    }
}
