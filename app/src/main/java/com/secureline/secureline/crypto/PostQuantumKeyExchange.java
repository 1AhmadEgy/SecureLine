package com.secureline.secureline.crypto;

import java.util.Arrays;

/**
 * PQC boundary.
 *
 * The previous implementation was XOR + SHA-256 over random values and was
 * not a post-quantum key exchange. This class deliberately fails closed until
 * the application is migrated to a vetted ML-KEM implementation.
 */
public final class PostQuantumKeyExchange {

    public PostQuantumKeyExchange() {
        throw new UnsupportedOperationException(
            "PQC is not enabled: migrate this call site to a vetted ML-KEM implementation."
        );
    }

    public byte[] getPublicKey() {
        throw unsupported();
    }

    public void computeSharedSecret(byte[] peerPublicKey) {
        throw unsupported();
    }

    public byte[] getSharedSecret() {
        throw unsupported();
    }

    public void clearKeys() {
        // No key material is created by this fail-closed implementation.
    }

    private static UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(
            "PQC is not enabled: migrate this call site to ML-KEM."
        );
    }
}
