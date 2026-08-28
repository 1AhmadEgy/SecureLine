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

public class SignalProtocolManager {

    private final SignalProtocolStore protocolStore;
    private final IdentityKeyPair identityKeyPair;
    private final int registrationId;

    public SignalProtocolManager(SignalProtocolStore store, IdentityKeyPair keyPair, int regId) {
        this.protocolStore = store;
        this.identityKeyPair = keyPair;
        this.registrationId = regId;
    }

    public byte[] encryptMessage(String remoteAddress, byte[] plaintext) {
        try {
            SignalProtocolAddress address = new SignalProtocolAddress(remoteAddress, 1);
            SessionCipher cipher = new SessionCipher(protocolStore, address);
            CiphertextMessage message = cipher.encrypt(plaintext);
            return message.serialize();
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] decryptMessage(String remoteAddress, byte[] ciphertext) {
        try {
            SignalProtocolAddress address = new SignalProtocolAddress(remoteAddress, 1);
            SessionCipher cipher = new SessionCipher(protocolStore, address);
            byte[] plaintext;
            try {
                SignalMessage message = new SignalMessage(ciphertext);
                plaintext = cipher.decrypt(message);
            } catch (Exception e1) {
                try {
                    PreKeySignalMessage message = new PreKeySignalMessage(ciphertext);
                    plaintext = cipher.decrypt(message);
                } catch (Exception e2) {
                    return null;
                }
            }
            return plaintext;
        } catch (Exception e) {
            return null;
        }
    }

    public void buildSession(String remoteAddress, PreKeyBundle preKeyBundle) {
        try {
            SignalProtocolAddress address = new SignalProtocolAddress(remoteAddress, 1);
            SessionBuilder builder = new SessionBuilder(protocolStore, address);
            builder.process(preKeyBundle);
        } catch (Exception e) {
            // Session build failed
        }
    }

    public IdentityKeyPair getIdentityKeyPair() {
        return identityKeyPair;
    }

    public int getRegistrationId() {
        return registrationId;
    }
}
