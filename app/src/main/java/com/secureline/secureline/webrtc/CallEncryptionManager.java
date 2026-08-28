package com.secureline.secureline.webrtc;

import com.secureline.secureline.crypto.KeyAgreementManager;

import java.util.HashMap;
import java.util.Map;

public class CallEncryptionManager {

    private final Map<String, CallEncryption> callEncryptions;
    private final Map<String, KeyAgreementManager> keyAgreements;

    public CallEncryptionManager() {
        callEncryptions = new HashMap<>();
        keyAgreements = new HashMap<>();
    }

    public void initializeCall(String callId) {
        KeyAgreementManager keyAgreement = new KeyAgreementManager();
        keyAgreements.put(callId, keyAgreement);
    }

    public byte[] getPublicKey(String callId) {
        KeyAgreementManager keyAgreement = keyAgreements.get(callId);
        if (keyAgreement == null) return null;
        return keyAgreement.getPublicKey();
    }

    public void establishEncryption(String callId, byte[] peerPublicKey) {
        KeyAgreementManager keyAgreement = keyAgreements.get(callId);
        if (keyAgreement == null) return;

        if (keyAgreement.computeSharedSecret(peerPublicKey)) {
            byte[] sessionKey = keyAgreement.deriveKey(32);
            if (sessionKey != null) {
                CallEncryption encryption = new CallEncryption(sessionKey);
                callEncryptions.put(callId, encryption);
            }
        }
    }

    public byte[] encryptCallData(String callId, byte[] data) {
        CallEncryption encryption = callEncryptions.get(callId);
        if (encryption == null) return null;
        return encryption.encryptAudio(data);
    }

    public byte[] decryptCallData(String callId, byte[] data) {
        CallEncryption encryption = callEncryptions.get(callId);
        if (encryption == null) return null;
        return encryption.decryptAudio(data);
    }

    public void endCall(String callId) {
        callEncryptions.remove(callId);
        keyAgreements.remove(callId);
    }

    public void endAllCalls() {
        callEncryptions.clear();
        keyAgreements.clear();
    }
}
