package com.secureline.secureline.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public class KeyExchangeManager {

    private KeyPair keyPair;
    private byte[] peerPublicKey;

    public KeyExchangeManager() {
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

    public void setPeerPublicKey(byte[] peerKey) {
        this.peerPublicKey = peerKey;
    }

    public byte[] computeSharedSecret() {
        if (keyPair == null || peerPublicKey == null) return null;
        try {
            javax.crypto.KeyAgreement agreement = javax.crypto.KeyAgreement.getInstance("X25519");
            agreement.init(keyPair.getPrivate());
            agreement.doPhase(decodePeerKey(peerPublicKey), true);
            return agreement.generateSecret();
        } catch (Exception e) {
            return null;
        }
    }

    private java.security.PublicKey decodePeerKey(byte[] keyBytes) {
        try {
            java.security.KeyFactory factory = java.security.KeyFactory.getInstance("X25519");
            java.security.spec.X509EncodedKeySpec spec = 
                new java.security.spec.X509EncodedKeySpec(keyBytes);
            return factory.generatePublic(spec);
        } catch (Exception e) {
            return null;
        }
    }

    public void rotateKeyPair() {
        generateKeyPair();
        peerPublicKey = null;
    }
}
