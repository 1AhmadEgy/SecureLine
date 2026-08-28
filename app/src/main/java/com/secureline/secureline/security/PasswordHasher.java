package com.secureline.secureline.security;

import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {

    private static final int ITERATIONS = 200000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_SIZE = 16;

    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verifyPassword(String password, byte[] salt, byte[] expectedHash) {
        byte[] computedHash = hashPassword(password, salt);
        if (computedHash == null || expectedHash == null) return false;
        if (computedHash.length != expectedHash.length) return false;
        return ByteUtils.constantTimeEquals(computedHash, expectedHash);
    }
}