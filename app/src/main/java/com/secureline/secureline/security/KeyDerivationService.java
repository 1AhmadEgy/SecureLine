package com.secureline.secureline.security;

import com.secureline.secureline.crypto.HKDF;
import com.secureline.secureline.crypto.HashUtils;

public class KeyDerivationService {

    public static byte[] deriveEncryptionKey(byte[] masterSecret, byte[] salt, String context) {
        return HKDF.deriveKey(masterSecret, salt, context.getBytes(), 32);
    }

    public static byte[] deriveAuthenticationKey(byte[] masterSecret, byte[] salt) {
        return HKDF.deriveKey(masterSecret, salt, "AUTH".getBytes(), 32);
    }

    public static byte[] deriveMacKey(byte[] masterSecret, byte[] salt) {
        return HKDF.deriveKey(masterSecret, salt, "MAC".getBytes(), 32);
    }

    public static byte[] deriveIvKey(byte[] masterSecret, byte[] salt) {
        return HKDF.deriveKey(masterSecret, salt, "IV".getBytes(), 16);
    }

    public static byte[] deriveSessionKeys(byte[] masterSecret, byte[] salt) {
        byte[] encryptionKey = deriveEncryptionKey(masterSecret, salt, "ENCRYPTION");
        byte[] authKey = deriveAuthenticationKey(masterSecret, salt);
        byte[] macKey = deriveMacKey(masterSecret, salt);
        byte[] ivKey = deriveIvKey(masterSecret, salt);

        byte[] allKeys = new byte[encryptionKey.length + authKey.length + macKey.length + ivKey.length];
        System.arraycopy(encryptionKey, 0, allKeys, 0, encryptionKey.length);
        System.arraycopy(authKey, 0, allKeys, encryptionKey.length, authKey.length);
        System.arraycopy(macKey, 0, allKeys, encryptionKey.length + authKey.length, macKey.length);
        System.arraycopy(ivKey, 0, allKeys, encryptionKey.length + authKey.length + macKey.length, ivKey.length);
        return allKeys;
    }
}
