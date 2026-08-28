package com.secureline.secureline.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

public class DoubleEncryption {

    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_SIZE = 12;

    public static byte[] encryptDouble(byte[] data, byte[] key1, byte[] key2) {
        byte[] firstLayer = encryptAES(data, key1);
        byte[] secondLayer = encryptAES(firstLayer, key2);
        return secondLayer;
    }

    public static byte[] decryptDouble(byte[] data, byte[] key1, byte[] key2) {
        byte[] firstLayer = decryptAES(data, key2);
        byte[] secondLayer = decryptAES(firstLayer, key1);
        return secondLayer;
    }

    private static byte[] encryptAES(byte[] data, byte[] key) {
        try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
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
            byte[] iv = Arrays.copyOfRange(data, 0, IV_SIZE);
            byte[] encrypted = Arrays.copyOfRange(data, IV_SIZE, data.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
