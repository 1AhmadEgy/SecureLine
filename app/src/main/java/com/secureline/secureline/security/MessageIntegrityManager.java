package com.secureline.secureline.security;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class MessageIntegrityManager {

    private final Map<String, byte[]> messageHashes;

    public MessageIntegrityManager() {
        messageHashes = new HashMap<>();
    }

    public byte[] computeHash(byte[] message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(message);
        } catch (Exception e) {
            return null;
        }
    }

    public void storeMessageHash(String messageId, byte[] message) {
        byte[] hash = computeHash(message);
        if (hash != null) {
            messageHashes.put(messageId, hash);
        }
    }

    public boolean verifyMessageIntegrity(String messageId, byte[] message) {
        byte[] storedHash = messageHashes.get(messageId);
        if (storedHash == null) return false;
        byte[] computedHash = computeHash(message);
        if (computedHash == null) return false;
        return java.security.MessageDigest.isEqual(storedHash, computedHash);
    }

    public void removeMessageHash(String messageId) {
        messageHashes.remove(messageId);
    }
}
