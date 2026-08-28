package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

    private final Map<String, Long> lastAttemptTimes;
    private final Map<String, Integer> attemptCounts;
    private final int maxAttempts;
    private final long windowMillis;

    public RateLimiter(int maxAttempts, long windowMillis) {
        this.lastAttemptTimes = new HashMap<>();
        this.attemptCounts = new HashMap<>();
        this.maxAttempts = maxAttempts;
        this.windowMillis = windowMillis;
    }

    public synchronized boolean allowRequest(String identifier) {
        long currentTime = System.currentTimeMillis();
        Long lastTime = lastAttemptTimes.get(identifier);
        Integer count = attemptCounts.get(identifier);

        if (lastTime == null || count == null) {
            lastAttemptTimes.put(identifier, currentTime);
            attemptCounts.put(identifier, 1);
            return true;
        }

        if (currentTime - lastTime > windowMillis) {
            lastAttemptTimes.put(identifier, currentTime);
            attemptCounts.put(identifier, 1);
            return true;
        }

        if (count >= maxAttempts) {
            return false;
        }

        attemptCounts.put(identifier, count + 1);
        return true;
    }

    public synchronized void reset(String identifier) {
        lastAttemptTimes.remove(identifier);
        attemptCounts.remove(identifier);
    }

    public synchronized void resetAll() {
        lastAttemptTimes.clear();
        attemptCounts.clear();
    }
}