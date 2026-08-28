package com.secureline.secureline.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyAgreement;

public class ECDHKeyExchange {

    private KeyPair keyPair;
    private byte[] sharedSecret;

    public ECDHKeyExchange() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
            generator.initialize(256);
            keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            keyPair = null;
        }
    }

    public byte[] getPublicKey() {
        if (keyPair == null) return null;
        return keyPair.getPublic().getEncoded();
    }

    public void computeSharedSecret(byte[] peerPublicKeyBytes) {
        try {
            java.security.KeyFactory factory = java.security.KeyFactory.getInstance("EC");
            java.security.spec.X509EncodedKeySpec spec = 
                new java.security.spec.X509EncodedKeySpec(peerPublicKeyBytes);
            PublicKey peerPublicKey = factory.generatePublic(spec);

            KeyAgreement agreement = KeyAgreement.getInstance("ECDH");
            agreement.init(keyPair.getPrivate());
            agreement.doPhase(peerPublicKey, true);
            sharedSecret = agreement.generateSecret();
        } catch (Exception e) {
            sharedSecret = null;
        }
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    public byte[] deriveSessionKey() {
        if (sharedSecret == null) return null;
        return HashUtils.sha256(sharedSecret);
    }
}
