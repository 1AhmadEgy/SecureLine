package com.secureline.secureline.crypto;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

public class IdentityManager {

    private IdentityKeyPair identityKeyPair;
    private int registrationId;

    public IdentityManager() {
        generateIdentity();
    }

    private void generateIdentity() {
        identityKeyPair = KeyHelper.generateIdentityKeyPair();
        registrationId = KeyHelper.generateRegistrationId(false);
    }

    public IdentityKeyPair getIdentityKeyPair() {
        return identityKeyPair;
    }

    public IdentityKey getIdentityKey() {
        return identityKeyPair.getPublicKey();
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public byte[] getPublicKeySerialized() {
        return identityKeyPair.getPublicKey().serialize();
    }

    public byte[] getPrivateKeySerialized() {
        return identityKeyPair.getPrivateKey().serialize();
    }

    public String getFingerprint() {
        return HashUtils.fingerprint(identityKeyPair.getPublicKey().serialize());
    }
}
