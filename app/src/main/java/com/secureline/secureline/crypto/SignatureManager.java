package com.secureline.secureline.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class SignatureManager {

    private KeyPair keyPair;

    public SignatureManager() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            keyPair = null;
        }
    }

    public byte[] sign(byte[] data) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate());
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean verify(byte[] data, byte[] signatureBytes) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(keyPair.getPublic());
            signature.update(data);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] getPublicKeyBytes() {
        if (keyPair == null) return null;
        return keyPair.getPublic().getEncoded();
    }

    public byte[] getPrivateKeyBytes() {
        if (keyPair == null) return null;
        return keyPair.getPrivate().getEncoded();
    }
}
