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
    private final SqlCipherSignalProtocolStore protocolStore;

    public PreKeyManager(
            IdentityKeyPair identityKeyPair,
            SqlCipherSignalProtocolStore protocolStore) {
        if (identityKeyPair == null || protocolStore == null) {
            throw new IllegalArgumentException("Identity and Signal store are required");
        }
        this.identityKeyPair = identityKeyPair;
        this.protocolStore = protocolStore;
    }

    public synchronized void generatePreKeys(int startId, int count) {
        if (startId < 1 || count < 1 || count > 1000) {
            throw new IllegalArgumentException("Invalid pre-key range");
        }

        List<PreKeyRecord> generated = KeyHelper.generatePreKeys(startId, count);
        for (PreKeyRecord record : generated) {
            protocolStore.storePreKey(record.getId(), record);
        }
    }

    public synchronized void generateSignedPreKey(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Invalid signed pre-key id");
        }
        SignedPreKeyRecord record = KeyHelper.generateSignedPreKey(id, identityKeyPair);
        protocolStore.storeSignedPreKey(id, record);
    }

    public synchronized PreKeyRecord getPreKey(int id) {
        if (id < 1 || !protocolStore.containsPreKey(id)) {
            return null;
        }
        try {
            return protocolStore.loadPreKey(id);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load Signal pre-key", e);
        }
    }

    public synchronized SignedPreKeyRecord getSignedPreKey() {
        List<SignedPreKeyRecord> records = protocolStore.loadSignedPreKeys();
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }

    public synchronized List<PreKeyRecord> getAllPreKeys() {
        List<PreKeyRecord> result = new ArrayList<>();
        for (int id = 1; id <= 1000; id++) {
            if (protocolStore.containsPreKey(id)) {
                try {
                    result.add(protocolStore.loadPreKey(id));
                } catch (Exception e) {
                    throw new IllegalStateException("Unable to load Signal pre-key", e);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized void removePreKey(int id) {
        if (id > 0) {
            protocolStore.removePreKey(id);
        }
    }
}
