package com.secureline.secureline.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.HashMap;
import java.util.Map;

public class EphemeralKeyManager {

    private final Map<String, KeyPair> ephemeralKeys;

    public EphemeralKeyManager() {
        ephemeralKeys = new HashMap<>();
    }

    public void generateEphemeralKey(String conversationId) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("X25519");
            KeyPair keyPair = generator.generateKeyPair();
            ephemeralKeys.put(conversationId, keyPair);
        } catch (Exception e) {
            // Failed to generate
        }
    }

    public byte[] getEphemeralPublicKey(String conversationId) {
        KeyPair keyPair = ephemeralKeys.get(conversationId);
        if (keyPair == null) return null;
        return keyPair.getPublic().getEncoded();
    }

    public byte[] getEphemeralPrivateKey(String conversationId) {
        KeyPair keyPair = ephemeralKeys.get(conversationId);
        if (keyPair == null) return null;
        return keyPair.getPrivate().getEncoded();
    }

    public void rotateEphemeralKey(String conversationId) {
        generateEphemeralKey(conversationId);
    }

    public void removeEphemeralKey(String conversationId) {
        ephemeralKeys.remove(conversationId);
    }

    public void clearAllEphemeralKeys() {
        ephemeralKeys.clear();
    }
}
