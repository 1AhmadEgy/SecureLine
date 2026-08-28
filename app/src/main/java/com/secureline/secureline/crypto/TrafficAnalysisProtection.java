package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.Random;

public class TrafficAnalysisProtection {

    private static final int MIN_PADDING = 64;
    private static final int MAX_PADDING = 512;
    private static final int BLOCK_SIZE = 128;

    public static byte[] padMessage(byte[] message) {
        SecureRandom random = new SecureRandom();
        int currentSize = message.length;
        int targetSize = ((currentSize / BLOCK_SIZE) + 1) * BLOCK_SIZE;
        int paddingSize = Math.min(targetSize - currentSize, MAX_PADDING);
        paddingSize = Math.max(paddingSize, MIN_PADDING);

        byte[] padded = new byte[currentSize + paddingSize + 1];
        System.arraycopy(message, 0, padded, 0, currentSize);
        random.nextBytes(java.util.Arrays.copyOfRange(padded, currentSize, padded.length - 1));
        padded[padded.length - 1] = (byte) paddingSize;
        return padded;
    }

    public static byte[] unpadMessage(byte[] paddedMessage) {
        if (paddedMessage.length < 2) return new byte[0];
        int paddingSize = paddedMessage[paddedMessage.length - 1] & 0xFF;
        int messageSize = paddedMessage.length - paddingSize - 1;
        if (messageSize < 0 || messageSize > paddedMessage.length) return new byte[0];
        return java.util.Arrays.copyOfRange(paddedMessage, 0, messageSize);
    }

    public static byte[] randomDelay() {
        Random random = new Random();
        byte[] delay = new byte[4];
        random.nextBytes(delay);
        return delay;
    }
}
