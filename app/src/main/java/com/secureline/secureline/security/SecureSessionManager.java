package com.secureline.secureline.security;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SecureSessionManager {

    private final Map<String, SecureSession> sessions;

    public SecureSessionManager() {
        sessions = new HashMap<>();
    }

    public String createSession(String userId) {
        String sessionId = generateSessionId();
        SecureSession session = new SecureSession(sessionId, userId);
        sessions.put(sessionId, session);
        return sessionId;
    }

    public SecureSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public boolean isValidSession(String sessionId) {
        SecureSession session = sessions.get(sessionId);
        if (session == null) return false;
        return !session.isExpired();
    }

    public void extendSession(String sessionId, long additionalMillis) {
        SecureSession session = sessions.get(sessionId);
        if (session != null) {
            session.extendExpiry(additionalMillis);
        }
    }

    public void terminateSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void terminateAllSessions() {
        sessions.clear();
    }

    public void terminateUserSessions(String userId) {
        sessions.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
    }

    private String generateSessionId() {
        byte[] random = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(random);
        StringBuilder sb = new StringBuilder();
        for (byte b : random) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static class SecureSession {
        private final String sessionId;
        private final String userId;
        private long expiryTime;
        private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000;

        public SecureSession(String sessionId, String userId) {
            this.sessionId = sessionId;
            this.userId = userId;
            this.expiryTime = System.currentTimeMillis() + DEFAULT_TIMEOUT;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getUserId() {
            return userId;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

        public void extendExpiry(long additionalMillis) {
            this.expiryTime = System.currentTimeMillis() + additionalMillis;
        }

        public long getRemainingTime() {
            return Math.max(0, expiryTime - System.currentTimeMillis());
        }
    }
}
