package com.secureline.secureline.security;

import android.content.Context;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class SecureStorageManager {

    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;

    private final Context context;
    private final byte[] storageKey;

    public SecureStorageManager(Context context, byte[] key) {
        this.context = context;
        this.storageKey = key;
    }

    public boolean storeEncrypted(String fileName, String data) {
        try {
            byte[] encrypted = encrypt(data.getBytes());
            if (encrypted == null) return false;
            java.io.FileOutputStream output = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            output.write(encrypted);
            output.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String loadEncrypted(String fileName) {
        try {
            java.io.FileInputStream input = context.openFileInput(fileName);
            byte[] encrypted = new byte[(int) input.available()];
            input.read(encrypted);
            input.close();
            byte[] decrypted = decrypt(encrypted);
            if (decrypted == null) return null;
            return new String(decrypted);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteFile(String fileName) {
        return context.deleteFile(fileName);
    }

    private byte[] encrypt(byte[] data) {
        try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(storageKey, "AES");
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
            SecretKeySpec keySpec = new SecretKeySpec(storageKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
