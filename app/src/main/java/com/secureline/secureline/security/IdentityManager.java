package com.secureline.secureline.security;

import com.secureline.secureline.crypto.HashUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class IdentityManager {

    private KeyPair identityKeyPair;
    private String identityId;

    public IdentityManager() {
        generateIdentity();
    }

    private void generateIdentity() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
            generator.initialize(256);
            identityKeyPair = generator.generateKeyPair();
            identityId = generateIdentityId(identityKeyPair.getPublic().getEncoded());
        } catch (Exception e) {
            identityKeyPair = null;
            identityId = null;
        }
    }

    private String generateIdentityId(byte[] publicKey) {
        byte[] hash = HashUtils.sha256(publicKey);
        if (hash == null) return null;
        return "user_" + HashUtils.sha256Hex(hash).substring(0, 32);
    }

    public String getIdentityId() {
        return identityId;
    }

    public byte[] getPublicKey() {
        if (identityKeyPair == null) return null;
        return identityKeyPair.getPublic().getEncoded();
    }

    public byte[] getPrivateKey() {
        if (identityKeyPair == null) return null;
        return identityKeyPair.getPrivate().getEncoded();
    }

    public String getFingerprint() {
        return FingerprintManager.generateFingerprint(getPublicKey());
    }
}
