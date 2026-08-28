package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class KeyRotationManager {

    private final Map<String, Long> lastRotationTimes;
    private final long rotationInterval;

    public KeyRotationManager(long rotationIntervalMillis) {
        this.lastRotationTimes = new HashMap<>();
        this.rotationInterval = rotationIntervalMillis;
    }

    public boolean shouldRotate(String keyId) {
        Long lastRotation = lastRotationTimes.get(keyId);
        if (lastRotation == null) return true;
        return System.currentTimeMillis() - lastRotation > rotationInterval;
    }

    public void markRotated(String keyId) {
        lastRotationTimes.put(keyId, System.currentTimeMillis());
    }

    public long getTimeSinceLastRotation(String keyId) {
        Long lastRotation = lastRotationTimes.get(keyId);
        if (lastRotation == null) return Long.MAX_VALUE;
        return System.currentTimeMillis() - lastRotation;
    }

    public void removeKey(String keyId) {
        lastRotationTimes.remove(keyId);
    }
}
