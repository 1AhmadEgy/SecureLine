package com.secureline.secureline.security;

import com.secureline.secureline.crypto.KeyAgreementManager;
import com.secureline.secureline.crypto.HashUtils;

public class SecureKeyExchange {

    private final KeyAgreementManager keyAgreement;
    private byte[] sessionKey;

    public SecureKeyExchange() {
        keyAgreement = new KeyAgreementManager();
    }

    public byte[] initiateExchange() {
        return keyAgreement.getPublicKey();
    }

    public boolean completeExchange(byte[] peerPublicKey) {
        if (keyAgreement.computeSharedSecret(peerPublicKey)) {
            sessionKey = keyAgreement.deriveKey(32);
            return sessionKey != null;
        }
        return false;
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public boolean isExchangeComplete() {
        return sessionKey != null;
    }

    public void resetExchange() {
        sessionKey = null;
        keyAgreement.clear();
    }

    public byte[] deriveEncryptionKey() {
        if (sessionKey == null) return null;
        return HashUtils.sha256(java.util.Arrays.copyOf(sessionKey, sessionKey.length));
    }
}
