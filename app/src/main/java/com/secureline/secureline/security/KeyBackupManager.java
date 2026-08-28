package com.secureline.secureline.security;

import java.security.SecureRandom;

public class KeyBackupManager {

    private static final int BACKUP_KEY_SIZE = 32;

    public static byte[] generateBackupKey() {
        byte[] key = new byte[BACKUP_KEY_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        return key;
    }

    public static String encodeBackupKey(byte[] key) {
        return android.util.Base64.encodeToString(key, android.util.Base64.NO_WRAP);
    }

    public static byte[] decodeBackupKey(String encodedKey) {
        return android.util.Base64.decode(encodedKey, android.util.Base64.NO_WRAP);
    }

    public static byte[] encryptBackup(byte[] data, byte[] backupKey) {
        try {
            byte[] iv = new byte[12];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
            javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(backupKey, "AES");
            javax.crypto.spec.GCMParameterSpec spec = new javax.crypto.spec.GCMParameterSpec(128, iv);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec, spec);

            byte[] encrypted = cipher.doFinal(data);
            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] decryptBackup(byte[] data, byte[] backupKey) {
        try {
            byte[] iv = new byte[12];
            System.arraycopy(data, 0, iv, 0, 12);

            byte[] encrypted = new byte[data.length - 12];
            System.arraycopy(data, 12, encrypted, 0, encrypted.length);

            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
            javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(backupKey, "AES");
            javax.crypto.spec.GCMParameterSpec spec = new javax.crypto.spec.GCMParameterSpec(128, iv);
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
