package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class ForwardSecretEncryption {

    private final Map<String, byte[]> chainKeys;

    public ForwardSecretEncryption() {
        chainKeys = new HashMap<>();
    }

    public void initializeChain(String conversationId, byte[] sharedSecret) {
        byte[] chainKey = HashUtils.sha256(sharedSecret);
        chainKeys.put(conversationId, chainKey);
    }

    public byte[] getNextMessageKey(String conversationId) {
        byte[] chainKey = chainKeys.get(conversationId);
        if (chainKey == null) return null;

        byte[] messageKey = HashUtils.sha256(chainKey);
        byte[] newChainKey = HashUtils.sha256(messageKey);
        chainKeys.put(conversationId, newChainKey);
        return messageKey;
    }

    public void advanceChain(String conversationId, int steps) {
        for (int i = 0; i < steps; i++) {
            getNextMessageKey(conversationId);
        }
    }

    public void removeChain(String conversationId) {
        chainKeys.remove(conversationId);
    }
}
