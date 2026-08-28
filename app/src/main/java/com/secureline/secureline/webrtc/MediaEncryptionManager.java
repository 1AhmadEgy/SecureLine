package com.secureline.secureline.webrtc;

import com.secureline.secureline.crypto.AESGCM;
import com.secureline.secureline.crypto.ObfuscationLayer;

import java.util.HashMap;
import java.util.Map;

public class MediaEncryptionManager {

    private final Map<String, byte[]> mediaKeys;

    public MediaEncryptionManager() {
        mediaKeys = new HashMap<>();
    }

    public void setMediaKey(String streamId, byte[] key) {
        mediaKeys.put(streamId, key);
    }

    public byte[] encryptMedia(String streamId, byte[] mediaData) {
        byte[] key = mediaKeys.get(streamId);
        if (key == null) return null;

        byte[] encrypted = AESGCM.encrypt(mediaData, key);
        if (encrypted == null) return null;

        return ObfuscationLayer.obfuscate(encrypted);
    }

    public byte[] decryptMedia(String streamId, byte[] obfuscatedData) {
        byte[] key = mediaKeys.get(streamId);
        if (key == null) return null;

        byte[] encrypted = ObfuscationLayer.deobfuscate(obfuscatedData);
        if (encrypted == null) return null;

        return AESGCM.decrypt(encrypted, key);
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
