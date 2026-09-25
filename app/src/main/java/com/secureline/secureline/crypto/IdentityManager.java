package com.secureline.secureline.crypto;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

public final class IdentityManager {

    private final IdentityKeyPair identityKeyPair;
    private final int registrationId;

    public IdentityManager() {
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
