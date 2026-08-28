package com.secureline.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    private final Map<String, UserRequestInfo> requestInfo;
    private final int maxRequests;
    private final long windowMillis;

    public RateLimiter(int maxRequests, long windowMillis) {
        this.requestInfo = new ConcurrentHashMap<>();
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
    }

    public boolean allowRequest(String clientId) {
        long currentTime = System.currentTimeMillis();
        UserRequestInfo info = requestInfo.get(clientId);

        if (info == null) {
            info = new UserRequestInfo(currentTime, 1);
            requestInfo.put(clientId, info);
            return true;
        }

        if (currentTime - info.windowStart > windowMillis) {
            info.windowStart = currentTime;
            info.requestCount = 1;
            return true;
        }

        if (info.requestCount >= maxRequests) {
            return false;
        }

        info.requestCount++;
        return true;
    }

    public void resetClient(String clientId) {
        requestInfo.remove(clientId);
    }

    public void resetAll() {
        requestInfo.clear();
    }

    private static class UserRequestInfo {
        long windowStart;
        int requestCount;

        UserRequestInfo(long start, int count) {
            this.windowStart = start;
            this.requestCount = count;
        }
    }
}
