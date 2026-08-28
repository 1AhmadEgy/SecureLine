package com.secureline.secureline.webrtc;

import java.util.ArrayList;
import java.util.List;

public class RtcpPacketManager {

    private final List<byte[]> rtcpPackets;

    public RtcpPacketManager() {
        rtcpPackets = new ArrayList<>();
    }

    public void addRtcpPacket(byte[] packet) {
        rtcpPackets.add(packet);
    }

    public byte[] getRtcpPacket(int index) {
        if (index < 0 || index >= rtcpPackets.size()) return null;
        return rtcpPackets.get(index);
    }

    public List<byte[]> getAllRtcpPackets() {
        return rtcpPackets;
    }

    public void clear() {
        rtcpPackets.clear();
    }
}
