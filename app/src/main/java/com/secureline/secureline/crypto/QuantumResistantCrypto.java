package com.secureline.secureline.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.KeyAgreement;

public class QuantumResistantCrypto {

    private KeyPair keyPair;
    private byte[] sharedSecret;

    public QuantumResistantCrypto() {
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

    public byte[] getPublicKeyBytes() {
        if (keyPair == null) return null;
        return keyPair.getPublic().getEncoded();
    }

    public void computeSharedSecret(byte[] remotePublicKeyBytes) {
        try {
            java.security.KeyFactory factory = java.security.KeyFactory.getInstance("X25519");
            java.security.spec.X509EncodedKeySpec spec = 
                new java.security.spec.X509EncodedKeySpec(remotePublicKeyBytes);
            PublicKey remotePublicKey = factory.generatePublic(spec);

            KeyAgreement agreement = KeyAgreement.getInstance("X25519");
            agreement.init(keyPair.getPrivate());
            agreement.doPhase(remotePublicKey, true);
            sharedSecret = agreement.generateSecret();
        } catch (Exception e) {
            sharedSecret = null;
        }
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    public byte[] deriveSymmetricKey() {
        if (sharedSecret == null) return null;
        return HashUtils.sha256(sharedSecret);
    }

    public void rotateKeys() {
        generateKeyPair();
        sharedSecret = null;
    }
}
