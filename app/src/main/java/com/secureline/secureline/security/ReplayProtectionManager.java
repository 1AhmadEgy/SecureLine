package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class ReplayProtectionManager {

    private final Map<String, Long> lastMessageTimestamps;
    private final long windowMillis;

    public ReplayProtectionManager(long windowMillis) {
        this.lastMessageTimestamps = new HashMap<>();
        this.windowMillis = windowMillis;
    }

    public boolean isReplay(String senderId, long timestamp) {
        Long lastTimestamp = lastMessageTimestamps.get(senderId);
        if (lastTimestamp == null) return false;
        return timestamp <= lastTimestamp || 
               (System.currentTimeMillis() - timestamp) > windowMillis;
    }

    public void recordMessage(String senderId, long timestamp) {
        lastMessageTimestamps.put(senderId, timestamp);
    }

    public void clearSender(String senderId) {
        lastMessageTimestamps.remove(senderId);
    }
}
