package com.secureline.secureline.network;

import com.secureline.secureline.crypto.ObfuscationLayer;
import com.secureline.secureline.crypto.SignalProtocolManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EncryptedMessageReceiver {

    private final SignalProtocolManager protocolManager;
    private final ExecutorService executorService;

    public EncryptedMessageReceiver(SignalProtocolManager manager) {
        this.protocolManager = manager;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void receiveEncryptedMessage(String senderId, byte[] obfuscatedData, 
                                         ReceiveListener listener) {
        executorService.execute(() -> {
            byte[] encrypted = ObfuscationLayer.deobfuscate(obfuscatedData);
            if (encrypted == null || encrypted.length == 0) {
                if (listener != null) listener.onError("Deobfuscation failed");
                return;
            }

            byte[] plaintext = protocolManager.decryptMessage(senderId, encrypted);
            if (plaintext == null) {
                if (listener != null) listener.onError("Decryption failed");
                return;
            }

            if (listener != null) listener.onSuccess(plaintext);
        });
    }

    public interface ReceiveListener {
        void onSuccess(byte[] plaintext);
        void onError(String error);
    }
}
