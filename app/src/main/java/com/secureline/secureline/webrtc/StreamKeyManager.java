package com.secureline.secureline.webrtc;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class StreamKeyManager {

    private final Map<String, byte[]> streamKeys;

    public StreamKeyManager() {
        streamKeys = new HashMap<>();
    }

    public byte[] createStreamKey(String streamId) {
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        streamKeys.put(streamId, key);
        return key;
    }

    public byte[] getStreamKey(String streamId) {
        return streamKeys.get(streamId);
    }

    public void rotateStreamKey(String streamId) {
        createStreamKey(streamId);
    }

    public void removeStreamKey(String streamId) {
        byte[] key = streamKeys.remove(streamId);
        if (key != null) {
            java.util.Arrays.fill(key, (byte) 0);
        }
    }

    public void clearAllKeys() {
        for (byte[] key : streamKeys.values()) {
            java.util.Arrays.fill(key, (byte) 0);
        }
        streamKeys.clear();
    }
}
