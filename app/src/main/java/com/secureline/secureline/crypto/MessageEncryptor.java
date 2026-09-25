package com.secureline.secureline.crypto;

public final class MessageEncryptor {

    private final byte[] encryptionKey;

    public MessageEncryptor(byte[] key) {
        if (key == null || key.length != 32) {
            throw new IllegalArgumentException("AES-256 key is required");
        }
        this.encryptionKey = key.clone();
    }

    public byte[] encrypt(byte[] plaintext) {
        return AESGCM.encrypt(plaintext, encryptionKey);
    }

    public byte[] decrypt(byte[] ciphertext) {
        return AESGCM.decrypt(ciphertext, encryptionKey);
    }
}
