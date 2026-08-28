package com.secureline.secureline.webrtc;

import com.secureline.secureline.crypto.KeyExchangeManager;

public class SignalNegotiator {

    private final KeyExchangeManager keyExchangeManager;
    private byte[] sessionKey;

    public SignalNegotiator() {
        keyExchangeManager = new KeyExchangeManager();
    }

    public byte[] initiateNegotiation() {
        return keyExchangeManager.getPublicKey();
    }

    public void receivePeerPublicKey(byte[] peerPublicKey) {
        keyExchangeManager.setPeerPublicKey(peerPublicKey);
        sessionKey = keyExchangeManager.computeSharedSecret();
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public boolean isNegotiationComplete() {
        return sessionKey != null;
    }

    public void resetNegotiation() {
        keyExchangeManager.rotateKeyPair();
        sessionKey = null;
    }
}
