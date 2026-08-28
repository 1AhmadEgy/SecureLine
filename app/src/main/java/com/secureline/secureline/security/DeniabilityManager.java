package com.secureline.secureline.security;

import com.secureline.secureline.crypto.DeniableEncryption;

import java.util.HashMap;
import java.util.Map;

public class DeniabilityManager {

    private final Map<String, byte[]> trueKeys;
    private final Map<String, byte[]> decoyKeys;

    public DeniabilityManager() {
        trueKeys = new HashMap<>();
        decoyKeys = new HashMap<>();
    }

    public void setKeys(String conversationId, byte[] trueKey, byte[] decoyKey) {
        trueKeys.put(conversationId, trueKey);
        decoyKeys.put(conversationId, decoyKey);
    }

    public byte[] encryptDeniable(String conversationId, byte[] plaintext) {
        byte[] trueKey = trueKeys.get(conversationId);
        byte[] decoyKey = decoyKeys.get(conversationId);
        if (trueKey == null || decoyKey == null) return null;
        return DeniableEncryption.encryptDeniable(plaintext, trueKey, decoyKey);
    }

    public byte[] decryptWithTrueKey(String conversationId, byte[] data) {
        byte[] trueKey = trueKeys.get(conversationId);
        if (trueKey == null) return null;
        return DeniableEncryption.decryptWithTrueKey(data, trueKey);
    }

    public byte[] decryptWithDecoyKey(String conversationId, byte[] data) {
        byte[] decoyKey = decoyKeys.get(conversationId);
        if (decoyKey == null) return null;
        return DeniableEncryption.decryptWithDecoyKey(data, decoyKey);
    }
}
