package com.secureline.secureline.security;

import com.secureline.secureline.crypto.AESGCM;
import com.secureline.secureline.crypto.HashUtils;

import java.security.SecureRandom;

public class SecureBackupManager {

    public static byte[] createBackup(byte[] data, String password) {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        byte[] key = HashUtils.sha256(
            java.util.Arrays.copyOf(salt, salt.length + password.length())
        );
        if (key == null) return null;

        byte[] encrypted = AESGCM.encrypt(data, key);
        if (encrypted == null) return null;

        byte[] backup = new byte[salt.length + encrypted.length];
        System.arraycopy(salt, 0, backup, 0, salt.length);
        System.arraycopy(encrypted, 0, backup, salt.length, encrypted.length);
        return backup;
    }

    public static byte[] restoreBackup(byte[] backup, String password) {
        if (backup.length < 16) return null;

        byte[] salt = new byte[16];
        System.arraycopy(backup, 0, salt, 0, 16);

        byte[] encrypted = new byte[backup.length - 16];
        System.arraycopy(backup, 16, encrypted, 0, encrypted.length);

        byte[] key = HashUtils.sha256(
            java.util.Arrays.copyOf(salt, salt.length + password.length())
        );
        if (key == null) return null;

        return AESGCM.decrypt(encrypted, key);
    }
}
