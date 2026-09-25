package com.secureline.server;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class SecureMessageHandler {

    private static final byte VERSION = 1;
    private static final int MAX_MESSAGE_BYTES = 1024 * 1024;
    private static final int RECIPIENT_LENGTH_BYTES = 2;

    private SecureMessageHandler() {}

    /**
     * Validates the transport envelope but does not decrypt it.
     * End-to-end encrypted payloads must remain opaque to the server.
     */
    public static byte[] processIncomingMessage(byte[] encryptedData) {
        validateEnvelope(encryptedData);
        return encryptedData.clone();
    }

    public static byte[] processOutgoingMessage(byte[] encryptedData) {
        validateEnvelope(encryptedData);
        return encryptedData.clone();
    }

    /**
     * Envelope format:
     * [version:1][recipientLength:2][recipient UTF-8][ciphertext:remaining]
     */
    public static String extractRecipientId(byte[] encryptedData) {
        validateEnvelope(encryptedData);
        ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
        buffer.get();
        int length = Short.toUnsignedInt(buffer.getShort());
        byte[] recipient = new byte[length];
        buffer.get(recipient);
        return new String(recipient, StandardCharsets.UTF_8);
    }

    private static void validateEnvelope(byte[] data) {
        if (data == null || data.length < 1 + RECIPIENT_LENGTH_BYTES + 1) {
            throw new IllegalArgumentException("Invalid encrypted message");
        }
        if (data.length > MAX_MESSAGE_BYTES) {
            throw new IllegalArgumentException("Encrypted message too large");
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        if (buffer.get() != VERSION) {
            throw new IllegalArgumentException("Unsupported message version");
        }

        int recipientLength = Short.toUnsignedInt(buffer.getShort());
        if (recipientLength == 0 || recipientLength > 4096 || buffer.remaining() <= recipientLength) {
            throw new IllegalArgumentException("Invalid recipient envelope");
        }

        byte[] recipient = new byte[recipientLength];
        buffer.get(recipient);
        String id = new String(recipient, StandardCharsets.UTF_8);
        if (!id.matches("[A-Za-z0-9._@+-]{1,4096}")) {
            throw new IllegalArgumentException("Invalid recipient id");
        }

        Arrays.fill(recipient, (byte) 0);
    }
}
