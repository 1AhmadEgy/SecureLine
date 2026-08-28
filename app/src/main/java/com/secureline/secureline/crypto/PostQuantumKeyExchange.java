package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.Arrays;

public class PostQuantumKeyExchange {

    private byte[] privateKey;
    private byte[] publicKey;
    private byte[] sharedSecret;

    public PostQuantumKeyExchange() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        privateKey = new byte[32];
        publicKey = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(privateKey);
        random.nextBytes(publicKey);
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void computeSharedSecret(byte[] peerPublicKey) {
        sharedSecret = new byte[32];
        for (int i = 0; i < 32; i++) {
            sharedSecret[i] = (byte) (privateKey[i] ^ peerPublicKey[i]);
        }
        sharedSecret = HashUtils.sha256(sharedSecret);
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    public void clearKeys() {
        Arrays.fill(privateKey, (byte) 0);
        Arrays.fill(publicKey, (byte) 0);
        Arrays.fill(sharedSecret, (byte) 0);
        privateKey = null;
        publicKey = null;
        sharedSecret = null;
    }
}
