package com.secureline.secureline.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class HybridEncryption {

    private static final int SESSION_KEY_SIZE = 32;
    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;

    public static byte[] encryptHybrid(byte[] plaintext, byte[] recipientPublicKey) {
        byte[] sessionKey = new byte[SESSION_KEY_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(sessionKey);

        byte[] encryptedData = encryptAES(plaintext, sessionKey);
        if (encryptedData == null) return null;

        byte[] encryptedSessionKey = QuantumResistantCryptoHelper.encryptWithPublicKey(
            sessionKey, recipientPublicKey
        );

        if (encryptedSessionKey == null) return null;

        byte[] result = new byte[4 + encryptedSessionKey.length + encryptedData.length];
        result[0] = (byte) ((encryptedSessionKey.length >> 24) & 0xFF);
        result[1] = (byte) ((encryptedSessionKey.length >> 16) & 0xFF);
        result[2] = (byte) ((encryptedSessionKey.length >> 8) & 0xFF);
        result[3] = (byte) (encryptedSessionKey.length & 0xFF);

        System.arraycopy(encryptedSessionKey, 0, result, 4, encryptedSessionKey.length);
        System.arraycopy(encryptedData, 0, result, 4 + encryptedSessionKey.length, encryptedData.length);

        return result;
    }

    public static byte[] decryptHybrid(byte[] hybridData, byte[] recipientPrivateKey) {
        if (hybridData.length < 4) return null;

        int sessionKeyLength = ((hybridData[0] & 0xFF) << 24) |
                               ((hybridData[1] & 0xFF) << 16) |
                               ((hybridData[2] & 0xFF) << 8) |
                               (hybridData[3] & 0xFF);

        if (hybridData.length < 4 + sessionKeyLength) return null;

        byte[] encryptedSessionKey = new byte[sessionKeyLength];
        System.arraycopy(hybridData, 4, encryptedSessionKey, 0, sessionKeyLength);

        byte[] encryptedData = new byte[hybridData.length - 4 - sessionKeyLength];
        System.arraycopy(hybridData, 4 + sessionKeyLength, encryptedData, 0, encryptedData.length);

        byte[] sessionKey = QuantumResistantCryptoHelper.decryptWithPrivateKey(
            encryptedSessionKey, recipientPrivateKey
        );

        if (sessionKey == null) return null;

        return decryptAES(encryptedData, sessionKey);
    }

    private static byte[] encryptAES(byte[] data, byte[] key) {
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

    private static byte[] decryptAES(byte[] data, byte[] key) {
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

class QuantumResistantCryptoHelper {
    static byte[] encryptWithPublicKey(byte[] data, byte[] publicKey) {
        // Placeholder for Kyber or other PQC algorithm
        return data;
    }

    static byte[] decryptWithPrivateKey(byte[] data, byte[] privateKey) {
        // Placeholder for Kyber or other PQC algorithm
        return data;
    }
}
