package com.secureline.secureline.database;

import com.secureline.secureline.crypto.ObfuscationLayer;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class MessageEncryptor {

    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;

    public static byte[] encryptForStorage(byte[] plaintext, byte[] storageKey) {
        if (plaintext == null || storageKey == null) return null;

        try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(storageKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

            byte[] encrypted = cipher.doFinal(plaintext);
            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

            return ObfuscationLayer.obfuscate(result);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] decryptFromStorage(byte[] obfuscatedData, byte[] storageKey) {
        if (obfuscatedData == null || storageKey == null) return null;

        try {
            byte[] data = ObfuscationLayer.deobfuscate(obfuscatedData);

            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(data, 0, iv, 0, IV_SIZE);

            byte[] encrypted = new byte[data.length - IV_SIZE];
            System.arraycopy(data, IV_SIZE, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(storageKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }
}