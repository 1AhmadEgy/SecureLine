package com.secureline.secureline.crypto;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Base64;

import com.secureline.secureline.database.DatabaseManager;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.PreKeyStore;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SQLCipher-backed implementation of all durable state required by libsignal.
 *
 * The database itself is protected by the Android Keystore-backed SQLCipher key.
 * No Signal private/session material is kept only in process memory.
 */
public final class SqlCipherSignalProtocolStore
        implements SignalProtocolStore {

    private final DatabaseManager databaseManager;
    private final IdentityKeyPair identityKeyPair;
    private final int registrationId;

    public SqlCipherSignalProtocolStore(
            DatabaseManager databaseManager,
            IdentityKeyPair identityKeyPair,
            int registrationId) {
        if (databaseManager == null || identityKeyPair == null) {
            throw new IllegalArgumentException("Database and identity are required");
        }
        if (registrationId <= 0) {
            throw new IllegalArgumentException("Invalid registration id");
        }
        this.databaseManager = databaseManager;
        this.identityKeyPair = identityKeyPair;
        this.registrationId = registrationId;
    }

    private SQLiteDatabase db() {
        return databaseManager.getSecureDatabase();
    }

    @Override
    public synchronized IdentityKeyPair getIdentityKeyPair() {
        return identityKeyPair;
    }

    @Override
    public synchronized int getLocalRegistrationId() {
        return registrationId;
    }

    @Override
    public synchronized IdentityKey getIdentity(SignalProtocolAddress address) {
        validateAddress(address);
        try (Cursor cursor = db().rawQuery(
                "SELECT identity_key FROM signal_identities " +
                "WHERE address_name = ? AND device_id = ?",
                new String[]{address.getName(), String.valueOf(address.getDeviceId())})) {
            if (!cursor.moveToFirst()) {
                return null;
            }
            return decodeIdentity(cursor.getString(0));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load remote Signal identity", e);
        }
    }

    @Override
    public synchronized boolean saveIdentity(
            SignalProtocolAddress address,
            IdentityKey identityKey) {
        validateAddress(address);
        if (identityKey == null) {
            throw new IllegalArgumentException("Identity key is required");
        }

        IdentityKey previous = getIdentity(address);
        if (previous != null && previous.equals(identityKey)) {
            return false;
        }

        long now = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put("address_name", address.getName());
        values.put("device_id", address.getDeviceId());
        values.put("identity_key", encode(identityKey.serialize()));
        values.put("created_at", now);
        values.put("updated_at", now);

        SQLiteDatabase database = db();
        long result = database.insertWithOnConflict(
                "signal_identities",
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1) {
            throw new IllegalStateException("Unable to persist remote Signal identity");
        }
        return previous != null;
    }

    @Override
    public synchronized boolean isTrustedIdentity(
            SignalProtocolAddress address,
            IdentityKey identityKey,
            IdentityKeyStore.Direction direction) {
        validateAddress(address);
        if (identityKey == null) {
            return false;
        }
        IdentityKey stored = getIdentity(address);
        // TOFU: an unseen identity is trusted on first use; a changed identity
        // is rejected until the user explicitly replaces the trusted identity.
        return stored == null || stored.equals(identityKey);
    }

    @Override
    public synchronized PreKeyRecord loadPreKey(int preKeyId)
            throws InvalidKeyIdException {
        try (Cursor cursor = db().rawQuery(
                "SELECT record FROM signal_pre_keys WHERE pre_key_id = ?",
                new String[]{String.valueOf(preKeyId)})) {
            if (!cursor.moveToFirst()) {
                throw new InvalidKeyIdException("No Signal pre-key: " + preKeyId);
            }
            return new PreKeyRecord(decode(cursor.getString(0)));
        } catch (InvalidKeyIdException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidKeyIdException(e);
        }
    }

    @Override
    public synchronized void storePreKey(int preKeyId, PreKeyRecord record) {
        if (record == null || preKeyId < 1) {
            throw new IllegalArgumentException("Invalid Signal pre-key");
        }
        ContentValues values = new ContentValues();
        values.put("pre_key_id", preKeyId);
        values.put("record", encode(record.serialize()));
        if (db().insertWithOnConflict(
                "signal_pre_keys", null, values, SQLiteDatabase.CONFLICT_REPLACE) == -1) {
            throw new IllegalStateException("Unable to persist Signal pre-key");
        }
    }

    @Override
    public synchronized boolean containsPreKey(int preKeyId) {
        try (Cursor cursor = db().rawQuery(
                "SELECT 1 FROM signal_pre_keys WHERE pre_key_id = ? LIMIT 1",
                new String[]{String.valueOf(preKeyId)})) {
            return cursor.moveToFirst();
        }
    }

    @Override
    public synchronized void removePreKey(int preKeyId) {
        db().delete("signal_pre_keys", "pre_key_id = ?",
                new String[]{String.valueOf(preKeyId)});
    }

    @Override
    public synchronized SessionRecord loadSession(SignalProtocolAddress address) {
        validateAddress(address);
        try (Cursor cursor = db().rawQuery(
                "SELECT record FROM signal_sessions " +
                "WHERE address_name = ? AND device_id = ?",
                new String[]{address.getName(), String.valueOf(address.getDeviceId())})) {
            if (!cursor.moveToFirst()) {
                return new SessionRecord();
            }
            return new SessionRecord(decode(cursor.getString(0)));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load Signal session", e);
        }
    }

    @Override
    public synchronized void storeSession(
            SignalProtocolAddress address,
            SessionRecord record) {
        validateAddress(address);
        if (record == null) {
            throw new IllegalArgumentException("Signal session record is required");
        }
        ContentValues values = new ContentValues();
        values.put("address_name", address.getName());
        values.put("device_id", address.getDeviceId());
        values.put("record", encode(record.serialize()));
        values.put("updated_at", System.currentTimeMillis());

        if (db().insertWithOnConflict(
                "signal_sessions", null, values, SQLiteDatabase.CONFLICT_REPLACE) == -1) {
            throw new IllegalStateException("Unable to persist Signal session");
        }
    }

    @Override
    public synchronized boolean containsSession(SignalProtocolAddress address) {
        validateAddress(address);
        try (Cursor cursor = db().rawQuery(
                "SELECT 1 FROM signal_sessions " +
                "WHERE address_name = ? AND device_id = ? LIMIT 1",
                new String[]{address.getName(), String.valueOf(address.getDeviceId())})) {
            return cursor.moveToFirst();
        }
    }

    @Override
    public synchronized void deleteSession(SignalProtocolAddress address) {
        validateAddress(address);
        db().delete(
                "signal_sessions",
                "address_name = ? AND device_id = ?",
                new String[]{address.getName(), String.valueOf(address.getDeviceId())});
    }

    @Override
    public synchronized void deleteAllSessions(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Signal address name is required");
        }
        db().delete("signal_sessions", "address_name = ?", new String[]{name});
    }

    @Override
    public synchronized List<Integer> getSubDeviceSessions(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Signal address name is required");
        }
        List<Integer> result = new ArrayList<>();
        try (Cursor cursor = db().rawQuery(
                "SELECT device_id FROM signal_sessions " +
                "WHERE address_name = ? ORDER BY device_id",
                new String[]{name})) {
            while (cursor.moveToNext()) {
                result.add(cursor.getInt(0));
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public synchronized SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId)
            throws InvalidKeyIdException {
        try (Cursor cursor = db().rawQuery(
                "SELECT record FROM signal_signed_pre_keys WHERE pre_key_id = ?",
                new String[]{String.valueOf(signedPreKeyId)})) {
            if (!cursor.moveToFirst()) {
                throw new InvalidKeyIdException(
                        "No Signal signed pre-key: " + signedPreKeyId);
            }
            return new SignedPreKeyRecord(decode(cursor.getString(0)));
        } catch (InvalidKeyIdException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidKeyIdException(e);
        }
    }

    @Override
    public synchronized List<SignedPreKeyRecord> loadSignedPreKeys() {
        List<SignedPreKeyRecord> result = new ArrayList<>();
        try (Cursor cursor = db().rawQuery(
                "SELECT record FROM signal_signed_pre_keys ORDER BY pre_key_id", null)) {
            while (cursor.moveToNext()) {
                try {
                    result.add(new SignedPreKeyRecord(decode(cursor.getString(0))));
                } catch (IOException e) {
                    throw new IllegalStateException(
                            "Corrupt Signal signed pre-key record", e);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public synchronized void storeSignedPreKey(
            int signedPreKeyId,
            SignedPreKeyRecord record) {
        if (record == null || signedPreKeyId < 1) {
            throw new IllegalArgumentException("Invalid Signal signed pre-key");
        }
        ContentValues values = new ContentValues();
        values.put("pre_key_id", signedPreKeyId);
        values.put("record", encode(record.serialize()));
        if (db().insertWithOnConflict(
                "signal_signed_pre_keys", null, values, SQLiteDatabase.CONFLICT_REPLACE) == -1) {
            throw new IllegalStateException("Unable to persist Signal signed pre-key");
        }
    }

    @Override
    public synchronized boolean containsSignedPreKey(int signedPreKeyId) {
        try (Cursor cursor = db().rawQuery(
                "SELECT 1 FROM signal_signed_pre_keys WHERE pre_key_id = ? LIMIT 1",
                new String[]{String.valueOf(signedPreKeyId)})) {
            return cursor.moveToFirst();
        }
    }

    @Override
    public synchronized void removeSignedPreKey(int signedPreKeyId) {
        db().delete(
                "signal_signed_pre_keys",
                "pre_key_id = ?",
                new String[]{String.valueOf(signedPreKeyId)});
    }

    private static void validateAddress(SignalProtocolAddress address) {
        if (address == null || address.getName() == null ||
                address.getName().isBlank() || address.getDeviceId() <= 0) {
            throw new IllegalArgumentException("Invalid Signal protocol address");
        }
    }

    private static String encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    private static byte[] decode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Empty persisted Signal record");
        }
        return Base64.decode(value, Base64.NO_WRAP);
    }

    private static IdentityKey decodeIdentity(String value) throws Exception {
        return new IdentityKey(decode(value), 0);
    }
}
