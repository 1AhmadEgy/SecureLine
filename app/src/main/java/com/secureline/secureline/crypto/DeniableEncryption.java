package com.secureline.secureline.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DeniableEncryption {

    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;

    public static byte[] encryptDeniable(byte[] plaintext, byte[] trueKey, byte[] decoyKey) {
        byte[] trueEncrypted = encrypt(plaintext, trueKey);
        byte[] decoyEncrypted = encrypt(plaintext, decoyKey);

        byte[] result = new byte[1 + trueEncrypted.length + decoyEncrypted.length];
        result[0] = (byte) trueEncrypted.length;

        System.arraycopy(trueEncrypted, 0, result, 1, trueEncrypted.length);
        System.arraycopy(decoyEncrypted, 0, result, 1 + trueEncrypted.length, decoyEncrypted.length);

        return result;
    }

    public static byte[] decryptWithTrueKey(byte[] deniableData, byte[] trueKey) {
        int trueLength = deniableData[0] & 0xFF;
        byte[] trueEncrypted = new byte[trueLength];
        System.arraycopy(deniableData, 1, trueEncrypted, 0, trueLength);
        return decrypt(trueEncrypted, trueKey);
    }

    public static byte[] decryptWithDecoyKey(byte[] deniableData, byte[] decoyKey) {
        int trueLength = deniableData[0] & 0xFF;
        byte[] decoyEncrypted = new byte[deniableData.length - 1 - trueLength];
        System.arraycopy(deniableData, 1 + trueLength, decoyEncrypted, 0, decoyEncrypted.length);
        return decrypt(decoyEncrypted, decoyKey);
    }

    private static byte[] encrypt(byte[] data, byte[] key) {
        try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

            byte[] encrypted = cipher.doFinal(data);
            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] decrypt(byte[] data, byte[] key) {
        try {
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(data, 0, iv, 0, IV_SIZE);

            byte[] encrypted = new byte[data.length - IV_SIZE];
            System.arraycopy(data, IV_SIZE, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
