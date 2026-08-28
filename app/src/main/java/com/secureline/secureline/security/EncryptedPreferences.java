package com.secureline.secureline.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedPreferences {

    private final SharedPreferences prefs;
    private final byte[] encryptionKey;

    public EncryptedPreferences(Context context, String name, byte[] key) {
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        this.encryptionKey = key;
    }

    public void putString(String key, String value) {
        byte[] encrypted = encrypt(value.getBytes());
        String encoded = Base64.encodeToString(encrypted, Base64.NO_WRAP);
        prefs.edit().putString(key, encoded).apply();
    }

    public String getString(String key, String defaultValue) {
        String encoded = prefs.getString(key, null);
        if (encoded == null) return defaultValue;
        byte[] encrypted = Base64.decode(encoded, Base64.NO_WRAP);
        byte[] decrypted = decrypt(encrypted);
        if (decrypted == null) return defaultValue;
        return new String(decrypted);
    }

    private byte[] encrypt(byte[] data) {
        try {
            byte[] iv = new byte[12];
            new java.security.SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
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

    private byte[] decrypt(byte[] data) {
        try {
            byte[] iv = new byte[12];
            System.arraycopy(data, 0, iv, 0, 12);

            byte[] encrypted = new byte[data.length - 12];
            System.arraycopy(data, 12, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
