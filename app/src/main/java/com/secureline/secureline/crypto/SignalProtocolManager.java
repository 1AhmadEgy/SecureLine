package com.secureline.secureline.crypto;

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

    public byte[] encryptMessage(String remoteAddress, byte[] plaintext) {
        requirePayload(remoteAddress, plaintext);
        try {
            SignalProtocolAddress address = new SignalProtocolAddress(remoteAddress, 1);
            SessionCipher cipher = new SessionCipher(protocolStore, address);
            CiphertextMessage message = cipher.encrypt(plaintext);
            return message.serialize();
        } catch (Exception e) {
            throw new IllegalStateException("Signal encryption failed", e);
        }
    }

    public byte[] decryptMessage(String remoteAddress, byte[] ciphertext) {
        requirePayload(remoteAddress, ciphertext);
        try {
            SignalProtocolAddress address = new SignalProtocolAddress(remoteAddress, 1);

            try {
                return new SessionCipher(protocolStore, address)
                        .decrypt(new PreKeySignalMessage(ciphertext));
            } catch (Exception ignored) {
                return new SessionCipher(protocolStore, address)
                        .decrypt(new SignalMessage(ciphertext));
            }
        } catch (Exception e) {
            throw new SecurityException("Signal decryption failed", e);
        }
    }

    public void buildSession(String remoteAddress, PreKeyBundle preKeyBundle) {
        if (remoteAddress == null || remoteAddress.isBlank() || preKeyBundle == null) {
            throw new IllegalArgumentException("Remote address and pre-key bundle are required");
        }
        try {
            SignalProtocolAddress address = new SignalProtocolAddress(remoteAddress, 1);
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

    private static void requirePayload(String address, byte[] payload) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Remote address is required");
        }
        if (payload == null || payload.length == 0) {
            throw new IllegalArgumentException("Payload is required");
        }
    }
}
