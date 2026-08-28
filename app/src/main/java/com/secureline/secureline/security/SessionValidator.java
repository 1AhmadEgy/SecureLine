package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class SessionValidator {

    private final Map<String, Long> sessionExpiries;

    public SessionValidator() {
        sessionExpiries = new HashMap<>();
    }

    public void createSession(String sessionId, long timeoutMillis) {
        sessionExpiries.put(sessionId, System.currentTimeMillis() + timeoutMillis);
    }

    public boolean isValidSession(String sessionId) {
        Long expiry = sessionExpiries.get(sessionId);
        if (expiry == null) return false;
        return System.currentTimeMillis() < expiry;
    }

    public void extendSession(String sessionId, long additionalMillis) {
        Long expiry = sessionExpiries.get(sessionId);
        if (expiry != null) {
            sessionExpiries.put(sessionId, System.currentTimeMillis() + additionalMillis);
        }
    }

    public void invalidateSession(String sessionId) {
        sessionExpiries.remove(sessionId);
    }

    public void invalidateAllSessions() {
        sessionExpiries.clear();
    }
}
