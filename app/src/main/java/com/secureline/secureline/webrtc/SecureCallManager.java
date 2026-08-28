package com.secureline.secureline.webrtc;

import com.secureline.secureline.crypto.KeyExchangeManager;

import java.util.HashMap;
import java.util.Map;

public class SecureCallManager {

    private final Map<String, SecureCallSession> callSessions;

    public SecureCallManager() {
        callSessions = new HashMap<>();
    }

    public void startSecureCall(String callId, String remoteUserId) {
        SecureCallSession session = new SecureCallSession(callId, remoteUserId);
        callSessions.put(callId, session);
    }

    public byte[] getPublicKey(String callId) {
        SecureCallSession session = callSessions.get(callId);
        if (session == null) return null;
        return session.getKeyExchangeManager().getPublicKey();
    }

    public boolean establishEncryption(String callId, byte[] peerPublicKey) {
        SecureCallSession session = callSessions.get(callId);
        if (session == null) return false;

        KeyExchangeManager keyExchange = session.getKeyExchangeManager();
        keyExchange.setPeerPublicKey(peerPublicKey);
        byte[] sharedSecret = keyExchange.computeSharedSecret();
        return sharedSecret != null;
    }

    public void endSecureCall(String callId) {
        callSessions.remove(callId);
    }

    public boolean isCallSecure(String callId) {
        SecureCallSession session = callSessions.get(callId);
        return session != null && session.isEncryptionEstablished();
    }

    private static class SecureCallSession {
        private final String callId;
        private final String remoteUserId;
        private final KeyExchangeManager keyExchangeManager;
        private boolean encryptionEstablished;

        SecureCallSession(String callId, String remoteUserId) {
            this.callId = callId;
            this.remoteUserId = remoteUserId;
            this.keyExchangeManager = new KeyExchangeManager();
            this.encryptionEstablished = false;
        }

        String getCallId() {
            return callId;
        }

        String getRemoteUserId() {
            return remoteUserId;
        }

        KeyExchangeManager getKeyExchangeManager() {
            return keyExchangeManager;
        }

        boolean isEncryptionEstablished() {
            return encryptionEstablished;
        }

        void setEncryptionEstablished(boolean established) {
            this.encryptionEstablished = established;
        }
    }
}
