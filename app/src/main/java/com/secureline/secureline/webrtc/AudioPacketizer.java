package com.secureline.secureline.webrtc;

import java.util.ArrayList;
import java.util.List;

public class AudioPacketizer {

    private static final int MAX_PACKET_SIZE = 1200;
    private static final int HEADER_SIZE = 12;

    private int sequenceNumber = 0;

    public List<byte[]> packetize(byte[] audioFrame, long timestamp) {
        List<byte[]> packets = new ArrayList<>();

        int payloadSize = MAX_PACKET_SIZE - HEADER_SIZE;
        int totalFrames = (int) Math.ceil((double) audioFrame.length / payloadSize);

        for (int i = 0; i < totalFrames; i++) {
            int start = i * payloadSize;
            int end = Math.min(start + payloadSize, audioFrame.length);
            int length = end - start;

            byte[] packet = new byte[HEADER_SIZE + length];

            packet[0] = (byte) ((sequenceNumber >> 8) & 0xFF);
            packet[1] = (byte) (sequenceNumber & 0xFF);
            packet[2] = (byte) ((timestamp >> 24) & 0xFF);
            packet[3] = (byte) ((timestamp >> 16) & 0xFF);
            packet[4] = (byte) ((timestamp >> 8) & 0xFF);
            packet[5] = (byte) (timestamp & 0xFF);
            packet[6] = (byte) (i & 0xFF);
            packet[7] = (byte) (totalFrames & 0xFF);
            packet[8] = (byte) ((length >> 8) & 0xFF);
            packet[9] = (byte) (length & 0xFF);
            packet[10] = 0;
            packet[11] = 0;

            System.arraycopy(audioFrame, start, packet, HEADER_SIZE, length);
            packets.add(packet);
        }

        sequenceNumber++;
        return packets;
    }

    public int getNextSequenceNumber() {
        return sequenceNumber;
    }
}
