package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.Arrays;

public final class MetadataProtection {

    private static final int PADDING_BLOCK = 256;
    private static final int MAX_PADDING = 255;
    private static final SecureRandom RANDOM = new SecureRandom();

    private MetadataProtection() {}

    /**
     * Pads to deterministic 256-byte buckets with at most 255 bytes of
     * randomized padding. The final byte stores the padding length.
     */
    public static byte[] padMessage(byte[] message) {
        if (message == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        int remainder = message.length % PADDING_BLOCK;
        int paddingSize = PADDING_BLOCK - remainder;
        if (paddingSize == 0) {
            paddingSize = PADDING_BLOCK;
        }
        paddingSize = Math.min(paddingSize, MAX_PADDING);

        byte[] padded = new byte[message.length + paddingSize];
        System.arraycopy(message, 0, padded, 0, message.length);

        if (paddingSize > 1) {
            byte[] randomPadding = new byte[paddingSize - 1];
            RANDOM.nextBytes(randomPadding);
            System.arraycopy(randomPadding, 0, padded, message.length, randomPadding.length);
        }

        padded[padded.length - 1] = (byte) paddingSize;
        return padded;
    }

    public static byte[] unpadMessage(byte[] paddedMessage) {
        if (paddedMessage == null || paddedMessage.length == 0) {
            throw new IllegalArgumentException("Invalid padded message");
        }

        int paddingSize = paddedMessage[paddedMessage.length - 1] & 0xFF;
        if (paddingSize < 1 || paddingSize > MAX_PADDING || paddingSize >= paddedMessage.length) {
            throw new IllegalArgumentException("Invalid padding");
        }

        return Arrays.copyOfRange(
            paddedMessage,
            0,
            paddedMessage.length - paddingSize
        );
    }

    public static int getPaddedSize(int originalSize) {
        if (originalSize < 0) {
            throw new IllegalArgumentException("originalSize must not be negative");
        }

        int remainder = originalSize % PADDING_BLOCK;
        int paddingSize = PADDING_BLOCK - remainder;
        if (paddingSize > MAX_PADDING) {
            paddingSize = MAX_PADDING;
        }
        return originalSize + paddingSize;
    }
}
