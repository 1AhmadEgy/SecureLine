package com.secureline.secureline.crypto;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

public class PBKDF2 {

    private static final int DEFAULT_ITERATIONS = 200000;
    private static final int DEFAULT_KEY_LENGTH = 256;

    public static byte[] deriveKey(String password, byte[] salt, int iterations, int keyLength) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                keyLength
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] deriveKey(String password, byte[] salt) {
        return deriveKey(password, salt, DEFAULT_ITERATIONS, DEFAULT_KEY_LENGTH);
    }

    public static byte[] generateSalt(int size) {
        byte[] salt = new byte[size];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}
