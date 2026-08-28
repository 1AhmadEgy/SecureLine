package com.secureline.secureline.util;

import com.secureline.secureline.crypto.AESGCM;
import com.secureline.secureline.crypto.HashUtils;
import com.secureline.secureline.crypto.ObfuscationLayer;

public class EncryptionUtils {

    public static byte[] encryptWithObfuscation(byte[] plaintext, byte[] key) {
        byte[] encrypted = AESGCM.encrypt(plaintext, key);
        if (encrypted == null) return null;
        return ObfuscationLayer.obfuscate(encrypted);
    }

    public static byte[] decryptWithObfuscation(byte[] obfuscatedData, byte[] key) {
        byte[] encrypted = ObfuscationLayer.deobfuscate(obfuscatedData);
        if (encrypted == null) return null;
        return AESGCM.decrypt(encrypted, key);
    }

    public static String encryptToString(byte[] plaintext, byte[] key) {
        byte[] encrypted = encryptWithObfuscation(plaintext, key);
        if (encrypted == null) return null;
        return android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP);
    }

    public static byte[] decryptFromString(String encryptedString, byte[] key) {
        byte[] encrypted = android.util.Base64.decode(encryptedString, android.util.Base64.NO_WRAP);
        return decryptWithObfuscation(encrypted, key);
    }

    public static byte[] deriveKeyFromPassword(String password, byte[] salt) {
        byte[] combined = new byte[salt.length + password.getBytes().length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(password.getBytes(), 0, combined, salt.length, password.getBytes().length);
        return HashUtils.sha256(combined);
    }
}
