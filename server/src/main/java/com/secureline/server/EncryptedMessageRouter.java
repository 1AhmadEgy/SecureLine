package com.secureline.server;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EncryptedMessageRouter {

    private final Map<String, Queue<byte[]>> messageQueues;

    public EncryptedMessageRouter() {
        messageQueues = new ConcurrentHashMap<>();
    }

    public void routeMessage(String recipientId, byte[] encryptedMessage) {
        Queue<byte[]> queue = messageQueues.computeIfAbsent(
            recipientId, k -> new ConcurrentLinkedQueue<>()
        );
        queue.offer(encryptedMessage);
    }

    public byte[] fetchNextMessage(String recipientId) {
        Queue<byte[]> queue = messageQueues.get(recipientId);
        if (queue == null) return null;
        return queue.poll();
    }

    public int getPendingMessageCount(String recipientId) {
        Queue<byte[]> queue = messageQueues.get(recipientId);
        return queue != null ? queue.size() : 0;
    }

    public void clearMessages(String recipientId) {
        Queue<byte[]> queue = messageQueues.get(recipientId);
        if (queue != null) {
            queue.clear();
        }
    }

    public void registerUser(String userId) {
        messageQueues.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>());
    }

    public void unregisterUser(String userId) {
        messageQueues.remove(userId);
    }
}
