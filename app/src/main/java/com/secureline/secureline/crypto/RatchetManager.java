package com.secureline.secureline.crypto;

import org.whispersystems.libsignal.ratchet.RatchetingSession;
import org.whispersystems.libsignal.ratchet.ChainKey;
import org.whispersystems.libsignal.ratchet.MessageKeys;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RatchetManager {

    private static final int MESSAGE_KEY_LENGTH = 32;
    private static final int MAC_KEY_LENGTH = 32;
    private static final int IV_LENGTH = 16;

    private ChainKey currentChainKey;

    public RatchetManager(byte[] sharedSecret) {
        byte[] derivedKey = deriveInitialKey(sharedSecret);
        this.currentChainKey = new ChainKey(derivedKey, 0);
    }

    private byte[] deriveInitialKey(byte[] sharedSecret) {
        byte[] salt = new byte[32];
        byte[] info = "SecureLine-Ratchet-v1".getBytes();
        return hkdfExtract(salt, sharedSecret, info);
    }

    public synchronized byte[] nextMessageKey() {
        MessageKeys keys = currentChainKey.getMessageKeys();
        currentChainKey = currentChainKey.getNextChainKey();
        return keys.getCipherKey().getEncoded();
    }

    public synchronized byte[] nextMacKey() {
        MessageKeys keys = currentChainKey.getMessageKeys();
        currentChainKey = currentChainKey.getNextChainKey();
        return keys.getMacKey().getEncoded();
    }

    public synchronized byte[] nextIv() {
        MessageKeys keys = currentChainKey.getMessageKeys();
        currentChainKey = currentChainKey.getNextChainKey();
        return keys.getIv().getIV();
    }

    private byte[] hkdfExtract(byte[] salt, byte[] input, byte[] info) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(salt, "HmacSHA256"));
            return mac.doFinal(input);
        } catch (Exception e) {
            return null;
        }
    }
}
