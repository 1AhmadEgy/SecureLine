package com.secureline.secureline.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;

public class HybridCryptoSystem {

    private KeyPair keyPair;
    private byte[] sessionKey;

    public HybridCryptoSystem() {
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
        return keyPair != null ? keyPair.getPublic().getEncoded() : null;
    }

    public void establishSession(byte[] peerPublicKey) {
        try {
            java.security.KeyFactory factory = java.security.KeyFactory.getInstance("X25519");
            java.security.spec.X509EncodedKeySpec spec = 
                new java.security.spec.X509EncodedKeySpec(peerPublicKey);
            PublicKey peerKey = factory.generatePublic(spec);

            KeyAgreement agreement = KeyAgreement.getInstance("X25519");
            agreement.init(keyPair.getPrivate());
            agreement.doPhase(peerKey, true);
            byte[] sharedSecret = agreement.generateSecret();
            sessionKey = HashUtils.sha256(sharedSecret);
        } catch (Exception e) {
            sessionKey = null;
        }
    }

    public byte[] encryptMessage(byte[] plaintext) {
        if (sessionKey == null) return null;
        return AESGCM.encrypt(plaintext, sessionKey);
    }

    public byte[] decryptMessage(byte[] ciphertext) {
        if (sessionKey == null) return null;
        return AESGCM.decrypt(ciphertext, sessionKey);
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }
}
