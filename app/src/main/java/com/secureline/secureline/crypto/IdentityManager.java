package com.secureline.secureline.crypto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Base64;

import com.secureline.secureline.database.DatabaseManager;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

public final class IdentityManager {

    private final IdentityKeyPair identityKeyPair;
    private final int registrationId;

    public IdentityManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context is required");
        }

        DatabaseManager database = DatabaseManager.getInstance(context);
        IdentityState state = load(database);

        if (state != null) {
            this.identityKeyPair = state.identityKeyPair;
            this.registrationId = state.registrationId;
            return;
        }

        IdentityKeyPair generatedPair = KeyHelper.generateIdentityKeyPair();
        int generatedRegistrationId = KeyHelper.generateRegistrationId(false);
        save(database, generatedPair, generatedRegistrationId);

        this.identityKeyPair = generatedPair;
        this.registrationId = generatedRegistrationId;
    }

    private static IdentityState load(DatabaseManager database) {
        try (Cursor cursor = database.getSecureDatabase().rawQuery(
                "SELECT identity_key_pair, registration_id FROM signal_identity WHERE id = 1",
                null)) {
            if (!cursor.moveToFirst()) {
                return null;
            }

            String encoded = cursor.getString(0);
            int registrationId = cursor.getInt(1);
            if (encoded == null || encoded.isBlank() || registrationId <= 0) {
                throw new IllegalStateException("Stored Signal identity is invalid");
            }

            byte[] serialized = Base64.decode(encoded, Base64.NO_WRAP);
            return new IdentityState(new IdentityKeyPair(serialized), registrationId);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load persistent Signal identity", e);
        }
    }

    private static void save(
            DatabaseManager database,
            IdentityKeyPair identityKeyPair,
            int registrationId) {
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put(
            "identity_key_pair",
            Base64.encodeToString(identityKeyPair.serialize(), Base64.NO_WRAP)
        );
        values.put("registration_id", registrationId);

        long result = database.getSecureDatabase().insertWithOnConflict(
            "signal_identity",
            null,
            values,
            android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
        );
        if (result == -1) {
            throw new IllegalStateException("Unable to persist Signal identity");
        }
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

    private static final class IdentityState {
        private final IdentityKeyPair identityKeyPair;
        private final int registrationId;

        private IdentityState(IdentityKeyPair identityKeyPair, int registrationId) {
            this.identityKeyPair = identityKeyPair;
            this.registrationId = registrationId;
        }
    }
}
