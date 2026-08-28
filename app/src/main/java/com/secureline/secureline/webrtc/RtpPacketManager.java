package com.secureline.secureline.webrtc;

import java.util.HashMap;
import java.util.Map;

public class RtpPacketManager {

    private final Map<Integer, byte[]> packetBuffer;
    private int expectedSequence;
    private int maxBufferSize;

    public RtpPacketManager(int maxBufferSize) {
        this.packetBuffer = new HashMap<>();
        this.expectedSequence = 0;
        this.maxBufferSize = maxBufferSize;
    }

    public void addPacket(int sequenceNumber, byte[] packet) {
        if (packetBuffer.size() >= maxBufferSize) {
            packetBuffer.clear();
        }
        packetBuffer.put(sequenceNumber, packet);
    }

    public byte[] getNextPacket() {
        byte[] packet = packetBuffer.remove(expectedSequence);
        if (packet != null) {
            expectedSequence++;
        }
        return packet;
    }

    public void setExpectedSequence(int sequence) {
        this.expectedSequence = sequence;
    }

    public int getExpectedSequence() {
        return expectedSequence;
    }

    public void clear() {
        packetBuffer.clear();
        expectedSequence = 0;
    }
}
