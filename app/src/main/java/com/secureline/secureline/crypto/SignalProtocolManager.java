package com.secureline.secureline.crypto;

import android.content.Context;

import com.secureline.secureline.database.DatabaseManager;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.protocol.SignalMessage;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.SignalProtocolStore;

public final class SignalProtocolManager {

    private final SignalProtocolStore protocolStore;
    private final IdentityKeyPair identityKeyPair;
    private final int registrationId;

    public SignalProtocolManager(
            SignalProtocolStore store,
            IdentityKeyPair keyPair,
            int registrationId) {
        if (store == null || keyPair == null) {
            throw new IllegalArgumentException("Signal store and identity key pair are required");
        }
        if (registrationId <= 0) {
            throw new IllegalArgumentException("Invalid Signal registration id");
        }
        this.protocolStore = store;
        this.identityKeyPair = keyPair;
        this.registrationId = registrationId;
    }

    /**
     * Creates a manager backed by the app's Keystore-protected SQLCipher database.
     */
    public static SignalProtocolManager create(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context is required");
        }
        IdentityManager identity = new IdentityManager(context);
        SqlCipherSignalProtocolStore store = new SqlCipherSignalProtocolStore(
                DatabaseManager.getInstance(context),
                identity.getIdentityKeyPair(),
                identity.getRegistrationId());
        return new SignalProtocolManager(
                store,
                identity.getIdentityKeyPair(),
                identity.getRegistrationId());
    }

    public byte[] encryptMessage(String remoteAddress, byte[] plaintext) {
        return encryptMessage(remoteAddress, 1, plaintext);
    }

    public byte[] encryptMessage(
            String remoteAddress,
            int deviceId,
            byte[] plaintext) {
        requirePayload(remoteAddress, deviceId, plaintext);
        try {
            SessionCipher cipher = new SessionCipher(
                    protocolStore,
                    new SignalProtocolAddress(remoteAddress, deviceId));
            CiphertextMessage message = cipher.encrypt(plaintext);
            return message.serialize();
        } catch (Exception e) {
            throw new IllegalStateException("Signal encryption failed", e);
        }
    }

    public byte[] decryptMessage(String remoteAddress, byte[] ciphertext) {
        return decryptMessage(remoteAddress, 1, ciphertext);
    }

    public byte[] decryptMessage(
            String remoteAddress,
            int deviceId,
            byte[] ciphertext) {
        requirePayload(remoteAddress, deviceId, ciphertext);
        try {
            SignalProtocolAddress address = new SignalProtocolAddress(remoteAddress, deviceId);
            SessionCipher cipher = new SessionCipher(protocolStore, address);

            if (ciphertext.length > 0 &&
                    (ciphertext[0] & 0xFF) == CiphertextMessage.PREKEY_TYPE) {
                return cipher.decrypt(new PreKeySignalMessage(ciphertext));
            }
            return cipher.decrypt(new SignalMessage(ciphertext));
        } catch (Exception e) {
            throw new SecurityException("Signal decryption failed", e);
        }
    }

    public void buildSession(String remoteAddress, PreKeyBundle preKeyBundle) {
        buildSession(remoteAddress, 1, preKeyBundle);
    }

    public void buildSession(
            String remoteAddress,
            int deviceId,
            PreKeyBundle preKeyBundle) {
        if (remoteAddress == null || remoteAddress.isBlank() ||
                deviceId <= 0 || preKeyBundle == null) {
            throw new IllegalArgumentException("Remote address, device and pre-key bundle are required");
        }
        try {
            SignalProtocolAddress address = new SignalProtocolAddress(remoteAddress, deviceId);
            new SessionBuilder(protocolStore, address).process(preKeyBundle);
        } catch (Exception e) {
            throw new IllegalStateException("Signal session establishment failed", e);
        }
    }

    public IdentityKeyPair getIdentityKeyPair() {
        return identityKeyPair;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public SignalProtocolStore getProtocolStore() {
        return protocolStore;
    }

    private static void requirePayload(String address, int deviceId, byte[] payload) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Remote address is required");
        }
        if (deviceId <= 0) {
            throw new IllegalArgumentException("Device id must be positive");
        }
        if (payload == null || payload.length == 0) {
            throw new IllegalArgumentException("Payload is required");
        }
    }
}
