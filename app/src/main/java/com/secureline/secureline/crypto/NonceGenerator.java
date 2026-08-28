package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class NonceGenerator {

    private final Set<String> usedNonces;
    private final SecureRandom random;

    public NonceGenerator() {
        usedNonces = new HashSet<>();
        random = new SecureRandom();
    }

    public byte[] generateNonce() {
        byte[] nonce = new byte[12];
        random.nextBytes(nonce);
        String nonceString = bytesToHex(nonce);
        while (usedNonces.contains(nonceString)) {
            random.nextBytes(nonce);
            nonceString = bytesToHex(nonce);
        }
        usedNonces.add(nonceString);
        return nonce;
    }

    public boolean isNonceUsed(byte[] nonce) {
        return usedNonces.contains(bytesToHex(nonce));
    }

    public void markNonceAsUsed(byte[] nonce) {
        usedNonces.add(bytesToHex(nonce));
    }

    public void clearNonces() {
        usedNonces.clear();
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
