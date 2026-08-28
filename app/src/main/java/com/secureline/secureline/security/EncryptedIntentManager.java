package com.secureline.secureline.security;

import android.content.Intent;

import com.secureline.secureline.crypto.AESGCM;

public class EncryptedIntentManager {

    public static void putEncryptedExtra(Intent intent, String key, String value, byte[] encryptionKey) {
        byte[] encrypted = AESGCM.encrypt(value.getBytes(), encryptionKey);
        if (encrypted != null) {
            intent.putExtra(key, android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP));
        }
    }

    public static String getEncryptedExtra(Intent intent, String key, byte[] encryptionKey, String defaultValue) {
        String encoded = intent.getStringExtra(key);
        if (encoded == null) return defaultValue;

        byte[] encrypted = android.util.Base64.decode(encoded, android.util.Base64.NO_WRAP);
        byte[] decrypted = AESGCM.decrypt(encrypted, encryptionKey);
        if (decrypted == null) return defaultValue;
        return new String(decrypted);
    }
}
