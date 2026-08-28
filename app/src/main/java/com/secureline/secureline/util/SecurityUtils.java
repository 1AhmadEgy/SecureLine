package com.secureline.secureline.util;

import java.security.SecureRandom;
import java.util.Arrays;

public class SecurityUtils {

    private static final SecureRandom random = new SecureRandom();

    public static byte[] generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] generateRandomKey() {
        return generateRandomBytes(32);
    }

    public static byte[] generateRandomIv() {
        return generateRandomBytes(12);
    }

    public static byte[] generateRandomSalt() {
        return generateRandomBytes(16);
    }

    public static void secureClear(byte[] data) {
        if (data != null) {
            Arrays.fill(data, (byte) 0);
        }
    }

    public static void secureClear(char[] data) {
        if (data != null) {
            Arrays.fill(data, '\0');
        }
    }

    public static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] fromHexString(String hex) {
        int length = hex.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) +
                                  Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
