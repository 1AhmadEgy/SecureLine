package com.secureline.secureline.crypto;

import java.security.MessageDigest;

public class HashUtils {

    public static byte[] sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String sha256Hex(byte[] data) {
        byte[] hash = sha256(data);
        if (hash == null) return null;
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String fingerprint(byte[] publicKey) {
        byte[] hash = sha256(publicKey);
        if (hash == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(16, hash.length); i++) {
            sb.append(String.format("%02x", hash[i]));
            if (i % 4 == 3 && i < 15) sb.append(" ");
        }
        return sb.toString();
    }
}
