package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.Arrays;

public class MetadataProtection {

    private static final int PADDING_BLOCK = 256;
    private static final int MAX_PADDING = 1024;

    public static byte[] padMessage(byte[] message) {
        SecureRandom random = new SecureRandom();
        int currentSize = message.length;
        int targetSize = ((currentSize / PADDING_BLOCK) + 1) * PADDING_BLOCK;
        
        if (targetSize - currentSize < 32) {
            targetSize += PADDING_BLOCK;
        }

        int paddingSize = Math.min(targetSize - currentSize, MAX_PADDING);
        byte[] padded = new byte[currentSize + paddingSize];
        System.arraycopy(message, 0, padded, 0, currentSize);
        random.nextBytes(Arrays.copyOfRange(padded, currentSize, padded.length));

        padded[currentSize] = (byte) (paddingSize & 0xFF);
        return padded;
    }

    public static byte[] unpadMessage(byte[] paddedMessage) {
        if (paddedMessage.length == 0) return new byte[0];
        int paddingSize = paddedMessage[paddedMessage.length - 1] & 0xFF;
        int messageSize = paddedMessage.length - paddingSize;
        return Arrays.copyOfRange(paddedMessage, 0, messageSize);
    }

    public static int getPaddedSize(int originalSize) {
        int targetSize = ((originalSize / PADDING_BLOCK) + 1) * PADDING_BLOCK;
        if (targetSize - originalSize < 32) {
            targetSize += PADDING_BLOCK;
        }
        return Math.min(targetSize - originalSize, MAX_PADDING) + originalSize;
    }
}
