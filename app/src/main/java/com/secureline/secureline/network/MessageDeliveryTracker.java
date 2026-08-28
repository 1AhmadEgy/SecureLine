package com.secureline.secureline.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageDeliveryTracker {

    public enum DeliveryStatus {
        PENDING,
        SENT,
        DELIVERED,
        READ,
        FAILED
    }

    private final Map<String, DeliveryStatus> messageStatuses;

    public MessageDeliveryTracker() {
        messageStatuses = new ConcurrentHashMap<>();
    }

    public void trackMessage(String messageId) {
        messageStatuses.put(messageId, DeliveryStatus.PENDING);
    }

    public void markSent(String messageId) {
        messageStatuses.put(messageId, DeliveryStatus.SENT);
    }

    public void markDelivered(String messageId) {
        messageStatuses.put(messageId, DeliveryStatus.DELIVERED);
    }

    public void markRead(String messageId) {
        messageStatuses.put(messageId, DeliveryStatus.READ);
    }

    public void markFailed(String messageId) {
        messageStatuses.put(messageId, DeliveryStatus.FAILED);
    }

    public DeliveryStatus getStatus(String messageId) {
        return messageStatuses.get(messageId);
    }

    public void removeTracking(String messageId) {
        messageStatuses.remove(messageId);
    }

    public void clearAll() {
        messageStatuses.clear();
    }
}