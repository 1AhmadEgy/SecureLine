package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class GroupEncryption {

    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;

    private final Map<String, byte[]> groupKeys;

    public GroupEncryption() {
        groupKeys = new HashMap<>();
    }

    public void createGroupKey(String groupId) {
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        groupKeys.put(groupId, key);
    }

    public void addGroupKey(String groupId, byte[] key) {
        groupKeys.put(groupId, key);
    }

    public void removeGroupKey(String groupId) {
        groupKeys.remove(groupId);
    }

    public byte[] encryptGroupMessage(String groupId, byte[] plaintext) {
        byte[] key = groupKeys.get(groupId);
        if (key == null) return null;

        try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

            byte[] encrypted = cipher.doFinal(plaintext);
            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] decryptGroupMessage(String groupId, byte[] ciphertext) {
        byte[] key = groupKeys.get(groupId);
        if (key == null) return null;

        try {
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(ciphertext, 0, iv, 0, IV_SIZE);

            byte[] encrypted = new byte[ciphertext.length - IV_SIZE];
            System.arraycopy(ciphertext, IV_SIZE, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }

    public void rotateGroupKey(String groupId) {
        createGroupKey(groupId);
    }

    public boolean hasGroupKey(String groupId) {
        return groupKeys.containsKey(groupId);
    }
}
