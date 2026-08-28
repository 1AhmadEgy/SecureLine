package com.secureline.secureline.webrtc;

import com.secureline.secureline.crypto.HashUtils;

import java.util.HashMap;
import java.util.Map;

public class MediaStreamKeyManager {

    private final Map<String, byte[]> streamKeys;

    public MediaStreamKeyManager() {
        streamKeys = new HashMap<>();
    }

    public void deriveStreamKey(String streamId, byte[] sharedSecret) {
        byte[] streamKey = HashUtils.sha256(sharedSecret);
        if (streamKey != null) {
            streamKeys.put(streamId, streamKey);
        }
    }

    public byte[] getStreamKey(String streamId) {
        return streamKeys.get(streamId);
    }

    public void removeStreamKey(String streamId) {
        byte[] key = streamKeys.remove(streamId);
        if (key != null) {
            java.util.Arrays.fill(key, (byte) 0);
        }
    }

    public void clearAllStreamKeys() {
        for (byte[] key : streamKeys.values()) {
            java.util.Arrays.fill(key, (byte) 0);
        }
        streamKeys.clear();
    }
}
