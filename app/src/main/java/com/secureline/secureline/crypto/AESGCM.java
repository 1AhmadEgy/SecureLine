package com.secureline.secureline.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

public final class AESGCM {

    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;
    private static final int KEY_SIZE = 32;

    private AESGCM() {}

    public static byte[] encrypt(byte[] plaintext, byte[] key) {
        requireKey(key);
        if (plaintext == null) {
            throw new IllegalArgumentException("Plaintext is required");
        }

        try {
            byte[] iv = new byte[IV_SIZE];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                Cipher.ENCRYPT_MODE,
                new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(TAG_BITS, iv)
            );

            byte[] encrypted = cipher.doFinal(plaintext);
            byte[] result = new byte[IV_SIZE + encrypted.length];
            System.arraycopy(iv, 0, result, 0, IV_SIZE);
            System.arraycopy(encrypted, 0, result, IV_SIZE, encrypted.length);
            return result;
        } catch (GeneralSecurityException e) {
            throw new SecurityException("AES-GCM encryption failed", e);
        }
    }

    public static byte[] decrypt(byte[] ciphertext, byte[] key) {
        requireKey(key);
        if (ciphertext == null || ciphertext.length <= IV_SIZE + 16) {
            throw new SecurityException("Invalid AES-GCM ciphertext");
        }

        try {
            byte[] iv = Arrays.copyOfRange(ciphertext, 0, IV_SIZE);
            byte[] encrypted = Arrays.copyOfRange(ciphertext, IV_SIZE, ciphertext.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                Cipher.DECRYPT_MODE,
                new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(TAG_BITS, iv)
            );
            return cipher.doFinal(encrypted);
        } catch (GeneralSecurityException e) {
            throw new SecurityException("AES-GCM authentication failed", e);
        }
    }

    private static void requireKey(byte[] key) {
        if (key == null || key.length != KEY_SIZE) {
            throw new IllegalArgumentException("AES-256 key is required");
        }
    }
}
