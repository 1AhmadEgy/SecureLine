package com.secureline.secureline.security;

import android.content.Context;
import android.content.SharedPreferences;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class SecurePreferenceManager {

    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;

    private final SharedPreferences prefs;
    private final byte[] encryptionKey;

    public SecurePreferenceManager(Context context, String prefName, byte[] key) {
        this.prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        this.encryptionKey = key;
    }

    public void putString(String key, String value) {
        byte[] encrypted = encrypt(value.getBytes());
        if (encrypted != null) {
            String encoded = android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP);
            prefs.edit().putString(key, encoded).apply();
        }
    }

    public String getString(String key, String defaultValue) {
        String encoded = prefs.getString(key, null);
        if (encoded == null) return defaultValue;

        byte[] encrypted = android.util.Base64.decode(encoded, android.util.Base64.NO_WRAP);
        byte[] decrypted = decrypt(encrypted);
        if (decrypted == null) return defaultValue;
        return new String(decrypted);
    }

    public void putBoolean(String key, boolean value) {
        putString(key, String.valueOf(value));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }

    public void putInt(String key, int value) {
        putString(key, String.valueOf(value));
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void remove(String key) {
        prefs.edit().remove(key).apply();
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }

    private byte[] encrypt(byte[] data) {
        try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, "AES");
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

    private byte[] decrypt(byte[] data) {
        try {
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(data, 0, iv, 0, IV_SIZE);

            byte[] encrypted = new byte[data.length - IV_SIZE];
            System.arraycopy(data, IV_SIZE, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
