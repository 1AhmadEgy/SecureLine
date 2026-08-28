package com.secureline.secureline.network;

import com.secureline.secureline.crypto.ObfuscationLayer;
import com.secureline.secureline.crypto.SignalProtocolManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EncryptedMessageSender {

    private final SignalProtocolManager protocolManager;
    private final ExecutorService executorService;
    private final MessageDeliveryTracker deliveryTracker;

    public EncryptedMessageSender(SignalProtocolManager manager) {
        this.protocolManager = manager;
        this.executorService = Executors.newFixedThreadPool(4);
        this.deliveryTracker = new MessageDeliveryTracker();
    }

    public void sendEncryptedMessage(String messageId, String recipientId, 
                                      byte[] plaintext, SendListener listener) {
        executorService.execute(() -> {
            deliveryTracker.trackMessage(messageId);
            deliveryTracker.markSent(messageId);

            byte[] encrypted = protocolManager.encryptMessage(recipientId, plaintext);
            if (encrypted == null) {
                deliveryTracker.markFailed(messageId);
                if (listener != null) listener.onError("Encryption failed");
                return;
            }

            byte[] obfuscated = ObfuscationLayer.obfuscate(encrypted);
            boolean sent = sendToServer(recipientId, obfuscated);
            if (sent) {
                deliveryTracker.markDelivered(messageId);
                if (listener != null) listener.onSuccess();
            } else {
                deliveryTracker.markFailed(messageId);
                if (listener != null) listener.onError("Send failed");
            }
        });
    }

    private boolean sendToServer(String recipientId, byte[] data) {
        // Implement network send
        return true;
    }

    public interface SendListener {
        void onSuccess();
        void onError(String error);
    }
}
