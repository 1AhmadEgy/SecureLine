package com.secureline.secureline.security;

import com.secureline.secureline.crypto.HashUtils;

import java.util.HashMap;
import java.util.Map;

public class DataIntegrityManager {

    private final Map<String, String> dataHashes;

    public DataIntegrityManager() {
        dataHashes = new HashMap<>();
    }

    public void storeHash(String dataId, byte[] data) {
        String hash = HashUtils.sha256Hex(data);
        if (hash != null) {
            dataHashes.put(dataId, hash);
        }
    }

    public boolean verifyIntegrity(String dataId, byte[] data) {
        String storedHash = dataHashes.get(dataId);
        if (storedHash == null) return false;
        String computedHash = HashUtils.sha256Hex(data);
        return storedHash.equals(computedHash);
    }

    public void removeHash(String dataId) {
        dataHashes.remove(dataId);
    }

    public void clearAllHashes() {
        dataHashes.clear();
    }
}
