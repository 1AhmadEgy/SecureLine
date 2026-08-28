package com.secureline.secureline.security;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class EncryptionKeyManager {

    private final Map<String, byte[]> encryptionKeys;

    public EncryptionKeyManager() {
        encryptionKeys = new HashMap<>();
    }

    public byte[] createKey(String keyId) {
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        encryptionKeys.put(keyId, key);
        return key;
    }

    public byte[] getKey(String keyId) {
        return encryptionKeys.get(keyId);
    }

    public void updateKey(String keyId, byte[] newKey) {
        byte[] oldKey = encryptionKeys.get(keyId);
        if (oldKey != null) {
            java.util.Arrays.fill(oldKey, (byte) 0);
        }
        encryptionKeys.put(keyId, newKey);
    }

    public void deleteKey(String keyId) {
        byte[] key = encryptionKeys.remove(keyId);
        if (key != null) {
            java.util.Arrays.fill(key, (byte) 0);
        }
    }

    public void clearAllKeys() {
        for (byte[] key : encryptionKeys.values()) {
            java.util.Arrays.fill(key, (byte) 0);
        }
        encryptionKeys.clear();
    }

    public boolean hasKey(String keyId) {
        return encryptionKeys.containsKey(keyId);
    }
}
