package com.secureline.secureline.webrtc;

import com.secureline.secureline.crypto.KeyExchangeManager;

import java.util.HashMap;
import java.util.Map;

public class CallKeyExchangeManager {

    private final Map<String, KeyExchangeManager> keyExchanges;

    public CallKeyExchangeManager() {
        keyExchanges = new HashMap<>();
    }

    public void startKeyExchange(String callId) {
        KeyExchangeManager manager = new KeyExchangeManager();
        keyExchanges.put(callId, manager);
    }

    public byte[] getPublicKey(String callId) {
        KeyExchangeManager manager = keyExchanges.get(callId);
        if (manager == null) return null;
        return manager.getPublicKey();
    }

    public byte[] computeSharedSecret(String callId, byte[] peerPublicKey) {
        KeyExchangeManager manager = keyExchanges.get(callId);
        if (manager == null) return null;
        manager.setPeerPublicKey(peerPublicKey);
        return manager.computeSharedSecret();
    }

    public void endKeyExchange(String callId) {
        keyExchanges.remove(callId);
    }
}
