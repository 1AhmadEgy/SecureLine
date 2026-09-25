package com.secureline.secureline.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class HybridEncryption {

    private static final int SESSION_KEY_SIZE = 32;
    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;

    /**
     * This method intentionally fails closed because the old implementation
     * claimed PQC while the helper simply returned the plaintext session key.
     */
    public static byte[] encryptHybrid(byte[] plaintext, byte[] recipientPublicKey) {
        throw new UnsupportedOperationException(
            "Hybrid PQC encryption is disabled until ML-KEM is integrated."
        );
    }

    public static byte[] decryptHybrid(byte[] hybridData, byte[] recipientPrivateKey) {
        throw new UnsupportedOperationException(
            "Hybrid PQC decryption is disabled until ML-KEM is integrated."
        );
    }

    private static byte[] encryptAES(byte[] data, byte[] key) {
        try {
            byte[] iv = new byte[IV_SIZE];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                Cipher.ENCRYPT_MODE,
                new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(TAG_BITS, iv)
            );

            byte[] encrypted = cipher.doFinal(data);
            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("AES-GCM encryption failed", e);
        }
    }

    private static byte[] decryptAES(byte[] data, byte[] key) {
        if (data == null || data.length < IV_SIZE + 16) {
            throw new IllegalArgumentException("Invalid AES-GCM payload");
        }

        try {
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(data, 0, iv, 0, IV_SIZE);

            byte[] encrypted = new byte[data.length - IV_SIZE];
            System.arraycopy(data, IV_SIZE, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                Cipher.DECRYPT_MODE,
                new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(TAG_BITS, iv)
            );
            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new IllegalArgumentException("AES-GCM authentication failed", e);
        }
    }
}
