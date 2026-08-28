package com.secureline.secureline.network;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {

    private final Queue<QueuedMessage> queue;

    public MessageQueue() {
        queue = new ConcurrentLinkedQueue<>();
    }

    public void enqueue(QueuedMessage message) {
        queue.offer(message);
    }

    public QueuedMessage dequeue() {
        return queue.poll();
    }

    public QueuedMessage peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }

    public static class QueuedMessage {
        private final String recipientId;
        private final byte[] encryptedBody;
        private final long timestamp;
        private final int priority;

        public QueuedMessage(String recipientId, byte[] encryptedBody, int priority) {
            this.recipientId = recipientId;
            this.encryptedBody = encryptedBody;
            this.timestamp = System.currentTimeMillis();
            this.priority = priority;
        }

        public String getRecipientId() {
            return recipientId;
        }

        public byte[] getEncryptedBody() {
            return encryptedBody;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public int getPriority() {
            return priority;
        }
    }
}