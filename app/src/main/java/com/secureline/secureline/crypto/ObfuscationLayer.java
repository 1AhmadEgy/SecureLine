package com.secureline.secureline.crypto;

/**
 * The old reverse-and-random-padding routine was not meaningful traffic
 * obfuscation and must not be treated as a security boundary.
 *
 * Transport confidentiality and metadata protection belong at the protocol
 * and transport layers (TLS, authenticated framing, and controlled padding).
 */
public final class ObfuscationLayer {

    private ObfuscationLayer() {}

    public static byte[] obfuscate(byte[] originalEncryptedData) {
        throw new UnsupportedOperationException(
            "Legacy obfuscation is disabled; use authenticated transport framing."
        );
    }

    public static byte[] deobfuscate(byte[] obfuscatedData) {
        throw new UnsupportedOperationException(
            "Legacy obfuscation is disabled; use authenticated transport framing."
        );
    }
}
