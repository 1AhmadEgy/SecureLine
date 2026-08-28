package com.secureline.secureline.security;

import java.security.SecureRandom;

public class TokenGenerator {

    private static final String TOKEN_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateToken(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(TOKEN_CHARS.length());
            sb.append(TOKEN_CHARS.charAt(index));
        }
        return sb.toString();
    }

    public static String generateHexToken(int byteCount) {
        return SecureRandomGenerator.generateHexString(byteCount);
    }

    public static String generateUuid() {
        return java.util.UUID.randomUUID().toString();
    }
}