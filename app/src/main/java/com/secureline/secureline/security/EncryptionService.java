package com.secureline.secureline.security;

import com.secureline.secureline.crypto.AESGCM;

public class EncryptionService {

    private byte[] encryptionKey;

    public EncryptionService(byte[] key) {
        this.encryptionKey = key;
    }

    public byte[] encrypt(byte[] plaintext) {
        if (encryptionKey == null || plaintext == null) return null;
        return AESGCM.encrypt(plaintext, encryptionKey);
    }

    public byte[] decrypt(byte[] ciphertext) {
        if (encryptionKey == null || ciphertext == null) return null;
        return AESGCM.decrypt(ciphertext, encryptionKey);
    }

    public void rotateKey(byte[] newKey) {
        if (encryptionKey != null) {
            java.util.Arrays.fill(encryptionKey, (byte) 0);
        }
        this.encryptionKey = newKey;
    }

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }
}
