package com.secureline.secureline.crypto;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class AESGCMTest {

    @Test
    public void encryptDecryptRoundTrip() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        byte[] plaintext = "SecureLine encrypted message".getBytes(StandardCharsets.UTF_8);

        byte[] ciphertext = AESGCM.encrypt(plaintext, key);
        byte[] decrypted = AESGCM.decrypt(ciphertext, key);

        assertArrayEquals(plaintext, decrypted);
    }

    @Test
    public void tamperingFailsAuthentication() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        byte[] ciphertext = AESGCM.encrypt("message".getBytes(StandardCharsets.UTF_8), key);

        ciphertext[ciphertext.length - 1] ^= 0x01;

        assertThrows(SecurityException.class, () -> AESGCM.decrypt(ciphertext, key));
    }

    @Test
    public void wrongKeyFailsAuthentication() {
        byte[] key = new byte[32];
        byte[] wrongKey = new byte[32];
        new SecureRandom().nextBytes(key);
        new SecureRandom().nextBytes(wrongKey);

        byte[] ciphertext = AESGCM.encrypt("message".getBytes(StandardCharsets.UTF_8), key);

        assertThrows(SecurityException.class, () -> AESGCM.decrypt(ciphertext, wrongKey));
    }
}
