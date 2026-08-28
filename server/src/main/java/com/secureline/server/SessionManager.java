package com.secureline.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private final Map<String, SessionInfo> sessions;

    public SessionManager() {
        sessions = new ConcurrentHashMap<>();
    }

    public String createSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        SessionInfo info = new SessionInfo(userId, System.currentTimeMillis());
        sessions.put(sessionId, info);
        return sessionId;
    }

    public boolean isValidSession(String sessionId) {
        SessionInfo info = sessions.get(sessionId);
        if (info == null) return false;

        long age = System.currentTimeMillis() - info.createdAt;
        if (age > 30 * 60 * 1000) {
            sessions.remove(sessionId);
            return false;
        }
        return true;
    }

    public String getUserId(String sessionId) {
        SessionInfo info = sessions.get(sessionId);
        return info != null ? info.userId : null;
    }

    public void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void invalidateAllSessions() {
        sessions.clear();
    }

    private static class SessionInfo {
        final String userId;
        final long createdAt;

        SessionInfo(String userId, long createdAt) {
            this.userId = userId;
            this.createdAt = createdAt;
        }
    }
}
