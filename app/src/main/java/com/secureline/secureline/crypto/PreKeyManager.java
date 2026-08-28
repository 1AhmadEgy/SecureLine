package com.secureline.secureline.crypto;

import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.ArrayList;
import java.util.List;

public class PreKeyManager {

    private final List<PreKeyRecord> preKeys;
    private SignedPreKeyRecord signedPreKey;

    public PreKeyManager() {
        preKeys = new ArrayList<>();
    }

    public void generatePreKeys(int startId, int count) {
        for (int i = startId; i < startId + count; i++) {
            PreKeyRecord record = KeyHelper.generatePreKeys(i, 1).get(0);
            preKeys.add(record);
        }
    }

    public void generateSignedPreKey(int id) {
        signedPreKey = KeyHelper.generateSignedPreKey(id, 
            IdentityManagerHolder.getIdentityKeyPair());
    }

    public PreKeyRecord getPreKey(int id) {
        for (PreKeyRecord record : preKeys) {
            if (record.getId() == id) {
                return record;
            }
        }
        return null;
    }

    public SignedPreKeyRecord getSignedPreKey() {
        return signedPreKey;
    }

    public List<PreKeyRecord> getAllPreKeys() {
        return preKeys;
    }

    public void removePreKey(int id) {
        preKeys.removeIf(record -> record.getId() == id);
    }
}

class IdentityManagerHolder {
    private static IdentityManager instance;

    public static org.whispersystems.libsignal.IdentityKeyPair getIdentityKeyPair() {
        if (instance == null) {
            instance = new IdentityManager();
        }
        return instance.getIdentityKeyPair();
    }
}
