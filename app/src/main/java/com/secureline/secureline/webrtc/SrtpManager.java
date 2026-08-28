package com.secureline.secureline.webrtc;

import java.security.SecureRandom;

public class SrtpManager {

    private byte[] masterKey;
    private byte[] masterSalt;

    public SrtpManager() {
        generateKeys();
    }

    private void generateKeys() {
        masterKey = new byte[16];
        masterSalt = new byte[14];
        SecureRandom random = new SecureRandom();
        random.nextBytes(masterKey);
        random.nextBytes(masterSalt);
    }

    public byte[] getMasterKey() {
        return masterKey;
    }

    public byte[] getMasterSalt() {
        return masterSalt;
    }

    public void setKeys(byte[] key, byte[] salt) {
        this.masterKey = key;
        this.masterSalt = salt;
    }

    public void rotateKeys() {
        generateKeys();
    }
}
