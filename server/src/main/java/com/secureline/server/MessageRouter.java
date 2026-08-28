package com.secureline.server;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageRouter {

    private final Map<String, Queue<byte[]>> messageQueues;

    public MessageRouter() {
        messageQueues = new ConcurrentHashMap<>();
    }

    public void routeMessage(String recipientId, byte[] encryptedMessage) {
        Queue<byte[]> queue = messageQueues.computeIfAbsent(
            recipientId, k -> new ConcurrentLinkedQueue<>()
        );
        queue.offer(encryptedMessage);
    }

    public byte[] fetchMessage(String recipientId) {
        Queue<byte[]> queue = messageQueues.get(recipientId);
        if (queue == null || queue.isEmpty()) return null;
        return queue.poll();
    }

    public void registerUser(String userId) {
        messageQueues.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>());
    }

    public void unregisterUser(String userId) {
        messageQueues.remove(userId);
    }

    public int getQueueSize(String userId) {
        Queue<byte[]> queue = messageQueues.get(userId);
        return queue != null ? queue.size() : 0;
    }
}
