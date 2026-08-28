package com.secureline.secureline.crypto;

import java.security.SecureRandom;

public class MessagePadding {

    private static final int[] PADDING_SIZES = {32, 64, 128, 256, 512, 1024};

    public static byte[] addRandomPadding(byte[] message) {
        SecureRandom random = new SecureRandom();
        int paddingSize = PADDING_SIZES[random.nextInt(PADDING_SIZES.length)];
        byte[] padded = new byte[message.length + paddingSize + 2];
        System.arraycopy(message, 0, padded, 0, message.length);
        random.nextBytes(java.util.Arrays.copyOfRange(padded, message.length, padded.length - 2));
        padded[padded.length - 2] = (byte) ((paddingSize >> 8) & 0xFF);
        padded[padded.length - 1] = (byte) (paddingSize & 0xFF);
        return padded;
    }

    public static byte[] removePadding(byte[] paddedMessage) {
        if (paddedMessage.length < 2) return new byte[0];
        int paddingSize = ((paddedMessage[paddedMessage.length - 2] & 0xFF) << 8) |
                          (paddedMessage[paddedMessage.length - 1] & 0xFF);
        int messageSize = paddedMessage.length - paddingSize - 2;
        if (messageSize < 0) return new byte[0];
        return java.util.Arrays.copyOfRange(paddedMessage, 0, messageSize);
    }
}