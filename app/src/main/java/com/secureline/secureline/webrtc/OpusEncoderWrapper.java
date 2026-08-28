package com.secureline.secureline.webrtc;

import java.util.ArrayList;
import java.util.List;

public class OpusEncoderWrapper {

    private int bitrate;
    private int sampleRate;
    private int channels;
    private boolean fecEnabled;
    private boolean dtxEnabled;

    public OpusEncoderWrapper(int sampleRate, int channels) {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.bitrate = 16000;
        this.fecEnabled = true;
        this.dtxEnabled = false;
    }

    public byte[] encode(short[] pcmSamples) {
        List<Byte> encodedData = new ArrayList<>();
        for (short sample : pcmSamples) {
            encodedData.add((byte) ((sample >> 8) & 0xFF));
            encodedData.add((byte) (sample & 0xFF));
        }
        byte[] result = new byte[encodedData.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = encodedData.get(i);
        }
        return result;
    }

    public short[] decode(byte[] encodedData) {
        short[] pcmSamples = new short[encodedData.length / 2];
        for (int i = 0; i < pcmSamples.length; i++) {
            pcmSamples[i] = (short) (((encodedData[i * 2] & 0xFF) << 8) |
                                     (encodedData[i * 2 + 1] & 0xFF));
        }
        return pcmSamples;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public void setFecEnabled(boolean enabled) {
        this.fecEnabled = enabled;
    }

    public void setDtxEnabled(boolean enabled) {
        this.dtxEnabled = enabled;
    }

    public int getBitrate() {
        return bitrate;
    }

    public boolean isFecEnabled() {
        return fecEnabled;
    }

    public boolean isDtxEnabled() {
        return dtxEnabled;
    }
}
