package com.secureline.secureline.webrtc;

import java.util.HashMap;
import java.util.Map;

public class JitterBuffer {

    private final Map<Integer, byte[]> packets;
    private int expectedSequenceNumber;
    private final int maxSize;

    public JitterBuffer(int maxSize) {
        packets = new HashMap<>();
        this.maxSize = maxSize;
        expectedSequenceNumber = 0;
    }

    public void addPacket(int sequenceNumber, byte[] data) {
        if (packets.size() >= maxSize) {
            return;
        }
        packets.put(sequenceNumber, data);
    }

    public byte[] getNextPacket() {
        byte[] packet = packets.remove(expectedSequenceNumber);
        if (packet != null) {
            expectedSequenceNumber++;
        }
        return packet;
    }

    public void reset() {
        packets.clear();
        expectedSequenceNumber = 0;
    }

    public int getBufferedPacketCount() {
        return packets.size();
    }
}
