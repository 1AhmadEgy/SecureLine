package com.secureline.secureline.security;

import com.secureline.secureline.crypto.HashUtils;

public class FingerprintManager {

    public static String generateFingerprint(byte[] publicKey) {
        byte[] hash = HashUtils.sha256(publicKey);
        if (hash == null) return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(String.format("%02x", hash[i]));
            if (i % 5 == 4 && i < 19) {
                sb.append(" ");
            }
        }
        return sb.toString().toUpperCase();
    }

    public static boolean compareFingerprints(String fingerprint1, String fingerprint2) {
        if (fingerprint1 == null || fingerprint2 == null) return false;
        return fingerprint1.replace(" ", "").equalsIgnoreCase(
               fingerprint2.replace(" ", "")
        );
    }

    public static String formatFingerprint(String fingerprint) {
        if (fingerprint == null) return null;
        String clean = fingerprint.replace(" ", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clean.length(); i++) {
            if (i > 0 && i % 5 == 0) {
                sb.append(" ");
            }
            sb.append(clean.charAt(i));
        }
        return sb.toString();
    }
}
