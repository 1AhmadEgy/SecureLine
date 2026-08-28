package com.secureline.secureline.security;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class KeyManagementService {

    private final Map<String, byte[]> keys;
    private final Map<String, Long> keyCreationTimes;

    public KeyManagementService() {
        keys = new HashMap<>();
        keyCreationTimes = new HashMap<>();
    }

    public byte[] generateKey(String keyId, int keySize) {
        byte[] key = new byte[keySize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        keys.put(keyId, key);
        keyCreationTimes.put(keyId, System.currentTimeMillis());
        return key;
    }

    public byte[] getKey(String keyId) {
        return keys.get(keyId);
    }

    public long getKeyAge(String keyId) {
        Long creationTime = keyCreationTimes.get(keyId);
        if (creationTime == null) return -1;
        return System.currentTimeMillis() - creationTime;
    }

    public boolean shouldRotateKey(String keyId, long maxAgeMillis) {
        long age = getKeyAge(keyId);
        return age > maxAgeMillis;
    }

    public void deleteKey(String keyId) {
        byte[] key = keys.remove(keyId);
        keyCreationTimes.remove(keyId);
        if (key != null) {
            java.util.Arrays.fill(key, (byte) 0);
        }
    }

    public void deleteAllKeys() {
        for (byte[] key : keys.values()) {
            java.util.Arrays.fill(key, (byte) 0);
        }
        keys.clear();
        keyCreationTimes.clear();
    }
}
