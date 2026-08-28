package com.secureline.secureline.crypto;

import java.util.Arrays;

public class KeyFingerprintVerifier {

    public static String generateFingerprint(byte[] publicKey) {
        byte[] hash = HashUtils.sha256(publicKey);
        if (hash == null) return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(String.format("%02x", hash[i]));
            if (i % 5 == 4 && i < 19) sb.append(" ");
        }
        return sb.toString();
    }

    public static boolean verifyFingerprint(byte[] publicKey, String expectedFingerprint) {
        String actualFingerprint = generateFingerprint(publicKey);
        if (actualFingerprint == null || expectedFingerprint == null) return false;

        String normalizedExpected = expectedFingerprint.replace(" ", "").toLowerCase();
        String normalizedActual = actualFingerprint.replace(" ", "").toLowerCase();
        return normalizedExpected.equals(normalizedActual);
    }

    public static byte[] generateFingerprintBytes(byte[] publicKey) {
        return Arrays.copyOf(HashUtils.sha256(publicKey), 20);
    }
}