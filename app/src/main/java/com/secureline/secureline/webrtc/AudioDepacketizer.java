package com.secureline.secureline.webrtc;

import java.util.HashMap;
import java.util.Map;

public class AudioDepacketizer {

    private final Map<Integer, byte[]> buffer;
    private int expectedSequence = -1;

    public AudioDepacketizer() {
        buffer = new HashMap<>();
    }

    public byte[] depacketize(byte[] packet) {
        if (packet.length < 12) return null;

        int sequence = ((packet[0] & 0xFF) << 8) | (packet[1] & 0xFF);
        int fragmentIndex = packet[6] & 0xFF;
        int totalFragments = packet[7] & 0xFF;
        int length = ((packet[8] & 0xFF) << 8) | (packet[9] & 0xFF);

        if (length > packet.length - 12) return null;

        byte[] payload = new byte[length];
        System.arraycopy(packet, 12, payload, 0, length);

        buffer.put(sequence, payload);

        if (allFragmentsReceived(sequence, totalFragments)) {
            byte[] completeFrame = assembleFragments(sequence, totalFragments);
            expectedSequence = sequence + 1;
            return completeFrame;
        }

        return null;
    }

    private boolean allFragmentsReceived(int sequence, int totalFragments) {
        int count = 0;
        for (int key : buffer.keySet()) {
            if (key == sequence) count++;
        }
        return count >= totalFragments;
    }

    private byte[] assembleFragments(int sequence, int totalFragments) {
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        for (int i = 0; i < totalFragments; i++) {
            byte[] fragment = buffer.remove(sequence);
            if (fragment != null) {
                output.write(fragment, 0, fragment.length);
            }
        }
        return output.toByteArray();
    }
}
