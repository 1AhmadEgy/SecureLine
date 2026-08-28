package com.secureline.server;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageStorageManager {

    private final Map<String, Queue<StoredMessage>> messageStore;

    public MessageStorageManager() {
        messageStore = new ConcurrentHashMap<>();
    }

    public void storeMessage(String recipientId, String senderId, byte[] encryptedBody) {
        StoredMessage message = new StoredMessage(senderId, encryptedBody);
        Queue<StoredMessage> queue = messageStore.computeIfAbsent(
            recipientId, k -> new ConcurrentLinkedQueue<>()
        );
        queue.offer(message);
    }

    public StoredMessage fetchNextMessage(String recipientId) {
        Queue<StoredMessage> queue = messageStore.get(recipientId);
        if (queue == null) return null;
        return queue.poll();
    }

    public int getStoredMessageCount(String recipientId) {
        Queue<StoredMessage> queue = messageStore.get(recipientId);
        return queue != null ? queue.size() : 0;
    }

    public void clearMessages(String recipientId) {
        Queue<StoredMessage> queue = messageStore.get(recipientId);
        if (queue != null) {
            queue.clear();
        }
    }

    public void clearAllMessages() {
        messageStore.clear();
    }

    public static class StoredMessage {
        private final String senderId;
        private final byte[] encryptedBody;
        private final long timestamp;

        public StoredMessage(String senderId, byte[] encryptedBody) {
            this.senderId = senderId;
            this.encryptedBody = encryptedBody;
            this.timestamp = System.currentTimeMillis();
        }

        public String getSenderId() {
            return senderId;
        }

        public byte[] getEncryptedBody() {
            return encryptedBody;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
