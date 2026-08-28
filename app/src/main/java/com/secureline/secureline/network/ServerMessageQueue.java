package com.secureline.secureline.network;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerMessageQueue {

    private final Queue<ServerMessage> messageQueue;

    public ServerMessageQueue() {
        messageQueue = new ConcurrentLinkedQueue<>();
    }

    public void enqueue(ServerMessage message) {
        messageQueue.offer(message);
    }

    public ServerMessage dequeue() {
        return messageQueue.poll();
    }

    public ServerMessage peek() {
        return messageQueue.peek();
    }

    public boolean isEmpty() {
        return messageQueue.isEmpty();
    }

    public int size() {
        return messageQueue.size();
    }

    public void clear() {
        messageQueue.clear();
    }

    public static class ServerMessage {
        private final String senderId;
        private final byte[] data;
        private final long timestamp;

        public ServerMessage(String senderId, byte[] data) {
            this.senderId = senderId;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        public String getSenderId() {
            return senderId;
        }

        public byte[] getData() {
            return data;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
