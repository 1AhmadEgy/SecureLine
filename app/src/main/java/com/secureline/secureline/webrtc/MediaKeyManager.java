package com.secureline.secureline.webrtc;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class MediaKeyManager {

    private final Map<String, byte[]> mediaKeys;

    public MediaKeyManager() {
        mediaKeys = new HashMap<>();
    }

    public byte[] generateMediaKey(String streamId) {
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        mediaKeys.put(streamId, key);
        return key;
    }

    public byte[] getMediaKey(String streamId) {
        return mediaKeys.get(streamId);
    }

    public void rotateMediaKey(String streamId) {
        generateMediaKey(streamId);
    }

    public void removeMediaKey(String streamId) {
        byte[] key = mediaKeys.remove(streamId);
        if (key != null) {
            java.util.Arrays.fill(key, (byte) 0);
        }
    }

    public void clearAllMediaKeys() {
        for (byte[] key : mediaKeys.values()) {
            java.util.Arrays.fill(key, (byte) 0);
        }
        mediaKeys.clear();
    }
}
