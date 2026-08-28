package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SessionKeyManager {

    private final Map<String, byte[]> sessionKeys;

    public SessionKeyManager() {
        sessionKeys = new HashMap<>();
    }

    public void createSessionKey(String sessionId) {
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        sessionKeys.put(sessionId, key);
    }

    public byte[] getSessionKey(String sessionId) {
        return sessionKeys.get(sessionId);
    }

    public void updateSessionKey(String sessionId) {
        createSessionKey(sessionId);
    }

    public void removeSessionKey(String sessionId) {
        sessionKeys.remove(sessionId);
    }

    public boolean hasSessionKey(String sessionId) {
        return sessionKeys.containsKey(sessionId);
    }

    public void clearAllKeys() {
        sessionKeys.clear();
    }
}
