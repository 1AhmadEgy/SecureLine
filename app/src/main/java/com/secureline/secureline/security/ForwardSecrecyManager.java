package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class ForwardSecrecyManager {

    private final Map<String, byte[]> sessionSecrets;

    public ForwardSecrecyManager() {
        sessionSecrets = new HashMap<>();
    }

    public void createSessionSecret(String sessionId, byte[] secret) {
        sessionSecrets.put(sessionId, secret);
    }

    public byte[] getSessionSecret(String sessionId) {
        return sessionSecrets.get(sessionId);
    }

    public void rotateSessionSecret(String sessionId, byte[] newSecret) {
        byte[] oldSecret = sessionSecrets.get(sessionId);
        if (oldSecret != null) {
            java.util.Arrays.fill(oldSecret, (byte) 0);
        }
        sessionSecrets.put(sessionId, newSecret);
    }

    public void deleteSessionSecret(String sessionId) {
        byte[] secret = sessionSecrets.remove(sessionId);
        if (secret != null) {
            java.util.Arrays.fill(secret, (byte) 0);
        }
    }

    public void clearAllSecrets() {
        for (byte[] secret : sessionSecrets.values()) {
            java.util.Arrays.fill(secret, (byte) 0);
        }
        sessionSecrets.clear();
    }
}
