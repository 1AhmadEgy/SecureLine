package com.secureline.secureline.crypto;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

public class KeyDerivation {

    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_SIZE = 16;

    public static byte[] deriveKey(String password, byte[] salt) {
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

    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}
