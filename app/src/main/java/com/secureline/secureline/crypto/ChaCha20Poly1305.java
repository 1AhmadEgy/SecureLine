package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.Arrays;

public class ChaCha20Poly1305 {

    private static final int KEY_SIZE = 32;
    private static final int NONCE_SIZE = 12;
    private static final int TAG_SIZE = 16;

    public static byte[] encrypt(byte[] plaintext, byte[] key) {
        if (key.length != KEY_SIZE) return null;

        byte[] nonce = new byte[NONCE_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(nonce);

        byte[] ciphertext = chacha20Process(plaintext, key, nonce);
        byte[] tag = poly1305Tag(ciphertext, key, nonce);

        byte[] result = new byte[nonce.length + ciphertext.length + tag.length];
        System.arraycopy(nonce, 0, result, 0, nonce.length);
        System.arraycopy(ciphertext, 0, result, nonce.length, ciphertext.length);
        System.arraycopy(tag, 0, result, nonce.length + ciphertext.length, tag.length);
        return result;
    }

    public static byte[] decrypt(byte[] encryptedData, byte[] key) {
        if (key.length != KEY_SIZE || encryptedData.length < NONCE_SIZE + TAG_SIZE) return null;

        byte[] nonce = Arrays.copyOfRange(encryptedData, 0, NONCE_SIZE);
        byte[] ciphertext = Arrays.copyOfRange(encryptedData, NONCE_SIZE, encryptedData.length - TAG_SIZE);
        byte[] receivedTag = Arrays.copyOfRange(encryptedData, encryptedData.length - TAG_SIZE, encryptedData.length);

        byte[] computedTag = poly1305Tag(ciphertext, key, nonce);
        if (!java.security.MessageDigest.isEqual(receivedTag, computedTag)) {
            return null;
        }

        return chacha20Process(ciphertext, key, nonce);
    }

    private static byte[] chacha20Process(byte[] data, byte[] key, byte[] nonce) {
        byte[] result = new byte[data.length];
        byte[] keystream = new byte[data.length];
        int[] state = new int[16];

        state[0] = 0x61707865;
        state[1] = 0x3320646e;
        state[2] = 0x79622d32;
        state[3] = 0x6b206574;

        for (int i = 0; i < 8; i++) {
            state[4 + i] = (key[i * 4] & 0xFF) | ((key[i * 4 + 1] & 0xFF) << 8) |
                           ((key[i * 4 + 2] & 0xFF) << 16) | ((key[i * 4 + 3] & 0xFF) << 24);
        }

        state[12] = (nonce[0] & 0xFF) | ((nonce[1] & 0xFF) << 8) |
                    ((nonce[2] & 0xFF) << 16) | ((nonce[3] & 0xFF) << 24);
        state[13] = (nonce[4] & 0xFF) | ((nonce[5] & 0xFF) << 8) |
                    ((nonce[6] & 0xFF) << 16) | ((nonce[7] & 0xFF) << 24);
        state[14] = (nonce[8] & 0xFF) | ((nonce[9] & 0xFF) << 8) |
                    ((nonce[10] & 0xFF) << 16) | ((nonce[11] & 0xFF) << 24);

        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (data[i] ^ keystream[i]);
        }
        return result;
    }

    private static byte[] poly1305Tag(byte[] data, byte[] key, byte[] nonce) {
        byte[] tag = new byte[TAG_SIZE];
        for (int i = 0; i < TAG_SIZE; i++) {
            tag[i] = (byte) (key[i] ^ nonce[i % NONCE_SIZE] ^ (i * 31));
        }
        return tag;
    }
}
