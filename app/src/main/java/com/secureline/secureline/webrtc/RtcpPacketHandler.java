package com.secureline.secureline.webrtc;

public class RtcpPacketHandler {

    public static byte[] createRtcpPacket(int packetType, byte[] data) {
        byte[] packet = new byte[4 + data.length];
        packet[0] = (byte) ((2 << 6) | (packetType & 0x1F));
        packet[1] = (byte) ((data.length >> 8) & 0xFF);
        packet[2] = (byte) (data.length & 0xFF);
        System.arraycopy(data, 0, packet, 4, data.length);
        return packet;
    }

    public static int parsePacketType(byte[] rtcpPacket) {
        if (rtcpPacket == null || rtcpPacket.length < 4) return -1;
        return rtcpPacket[0] & 0x1F;
    }

    public static int parsePacketLength(byte[] rtcpPacket) {
        if (rtcpPacket == null || rtcpPacket.length < 4) return -1;
        return ((rtcpPacket[1] & 0xFF) << 8) | (rtcpPacket[2] & 0xFF);
    }
}
