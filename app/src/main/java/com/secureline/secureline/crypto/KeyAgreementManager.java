package com.secureline.secureline.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyAgreement;

public class KeyAgreementManager {

    private KeyPair keyPair;
    private byte[] sharedSecret;

    public KeyAgreementManager() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("X25519");
            keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            keyPair = null;
        }
    }

    public byte[] getPublicKey() {
        if (keyPair == null) return null;
        return keyPair.getPublic().getEncoded();
    }

    public byte[] getPrivateKey() {
        if (keyPair == null) return null;
        return keyPair.getPrivate().getEncoded();
    }

    public boolean computeSharedSecret(byte[] peerPublicKeyBytes) {
        try {
            java.security.KeyFactory factory = java.security.KeyFactory.getInstance("X25519");
            java.security.spec.X509EncodedKeySpec spec = 
                new java.security.spec.X509EncodedKeySpec(peerPublicKeyBytes);
            PublicKey peerPublicKey = factory.generatePublic(spec);

            KeyAgreement agreement = KeyAgreement.getInstance("X25519");
            agreement.init(keyPair.getPrivate());
            agreement.doPhase(peerPublicKey, true);
            sharedSecret = agreement.generateSecret();
            return true;
        } catch (Exception e) {
            sharedSecret = null;
            return false;
        }
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    public byte[] deriveKey(int keySize) {
        if (sharedSecret == null) return null;
        byte[] hash = HashUtils.sha256(sharedSecret);
        if (hash == null) return null;
        return java.util.Arrays.copyOf(hash, keySize);
    }

    public void clear() {
        if (sharedSecret != null) {
            java.util.Arrays.fill(sharedSecret, (byte) 0);
        }
        sharedSecret = null;
        generateKeyPair();
    }
}
