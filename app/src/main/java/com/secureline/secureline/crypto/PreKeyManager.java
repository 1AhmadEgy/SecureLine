package com.secureline.secureline.crypto;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PreKeyManager {

    private final IdentityKeyPair identityKeyPair;
    private final List<PreKeyRecord> preKeys = new ArrayList<>();
    private SignedPreKeyRecord signedPreKey;

    public PreKeyManager(IdentityKeyPair identityKeyPair) {
        if (identityKeyPair == null) {
            throw new IllegalArgumentException("Identity key pair is required");
        }
        this.identityKeyPair = identityKeyPair;
    }

    public synchronized void generatePreKeys(int startId, int count) {
        if (startId < 1 || count < 1 || count > 1000) {
            throw new IllegalArgumentException("Invalid pre-key range");
        }
        preKeys.clear();
        preKeys.addAll(KeyHelper.generatePreKeys(startId, count));
    }

    public synchronized void generateSignedPreKey(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Invalid signed pre-key id");
        }
        signedPreKey = KeyHelper.generateSignedPreKey(id, identityKeyPair);
    }

    public synchronized PreKeyRecord getPreKey(int id) {
        for (PreKeyRecord record : preKeys) {
            if (record.getId() == id) {
                return record;
            }
        }
        return null;
    }

    public synchronized SignedPreKeyRecord getSignedPreKey() {
        return signedPreKey;
    }

    public synchronized List<PreKeyRecord> getAllPreKeys() {
        return Collections.unmodifiableList(new ArrayList<>(preKeys));
    }

    public synchronized void removePreKey(int id) {
        preKeys.removeIf(record -> record.getId() == id);
    }
}
