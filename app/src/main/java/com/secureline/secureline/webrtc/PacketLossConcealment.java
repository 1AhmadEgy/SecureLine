package com.secureline.secureline.webrtc;

public class PacketLossConcealment {

    private byte[] lastPacket;
    private boolean lastPacketValid;

    public PacketLossConcealment() {
        lastPacket = null;
        lastPacketValid = false;
    }

    public byte[] concealLoss() {
        if (!lastPacketValid || lastPacket == null) {
            return new byte[0];
        }
        return lastPacket.clone();
    }

    public void updateLastPacket(byte[] packet) {
        this.lastPacket = packet.clone();
        this.lastPacketValid = true;
    }

    public void reset() {
        lastPacket = null;
        lastPacketValid = false;
    }
}
