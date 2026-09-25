package com.secureline.secureline.crypto;

/**
 * Reserved for a formally specified deniable-encryption protocol.
 *
 * Concatenating two AES-GCM ciphertexts does not provide cryptographic
 * deniability, so the previous implementation is intentionally disabled.
 */
public final class DeniableEncryption {

    private DeniableEncryption() {}

    public static byte[] encryptDeniable(byte[] plaintext, byte[] trueKey, byte[] decoyKey) {
        throw unsupported();
    }

    public static byte[] decryptWithTrueKey(byte[] deniableData, byte[] trueKey) {
        throw unsupported();
    }

    public static byte[] decryptWithDecoyKey(byte[] deniableData, byte[] decoyKey) {
        throw unsupported();
    }

    private static UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(
            "Deniable encryption is disabled until a formally specified protocol is implemented."
        );
    }
}
