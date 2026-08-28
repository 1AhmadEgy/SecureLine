package com.secureline.secureline.crypto;

import org.whispersystems.libsignal.ratchet.RatchetingSession;

import javax.crypto.KeyAgreement;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

public class PerfectForwardSecrecy {

    private KeyPair ephemeralKeyPair;
    private byte[] sharedSecret;

    public PerfectForwardSecrecy() {
        generateEphemeralKeyPair();
    }

    private void generateEphemeralKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("X25519");
            ephemeralKeyPair = generator.generateKeyPair();
        } catch (Exception e) {
            ephemeralKeyPair = null;
        }
    }

    public byte[] getEphemeralPublicKey() {
        if (ephemeralKeyPair == null) return null;
        return ephemeralKeyPair.getPublic().getEncoded();
    }

    public void computeSharedSecret(byte[] remotePublicKey) {
        try {
            KeyAgreement agreement = KeyAgreement.getInstance("X25519");
            agreement.init(ephemeralKeyPair.getPrivate());
            agreement.doPhase(decodePublicKey(remotePublicKey), true);
            sharedSecret = agreement.generateSecret();
        } catch (Exception e) {
            sharedSecret = null;
        }
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    public void rotateEphemeralKey() {
        generateEphemeralKeyPair();
        sharedSecret = null;
    }

    private PublicKey decodePublicKey(byte[] keyBytes) {
        try {
            java.security.KeyFactory factory = java.security.KeyFactory.getInstance("X25519");
            java.security.spec.X509EncodedKeySpec spec = new java.security.spec.X509EncodedKeySpec(keyBytes);
            return factory.generatePublic(spec);
        } catch (Exception e) {
            return null;
        }
    }
}
