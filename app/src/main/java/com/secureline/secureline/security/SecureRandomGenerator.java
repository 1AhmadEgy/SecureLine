package com.secureline.secureline.security;

import java.security.SecureRandom;

public class SecureRandomGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static byte[] generateBytes(int size) {
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String generateHexString(int byteCount) {
        byte[] bytes = generateBytes(byteCount);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static int generateInt(int bound) {
        return random.nextInt(bound);
    }

    public static long generateLong() {
        return random.nextLong();
    }

    public static void reseed() {
        random.reseed();
    }
}
