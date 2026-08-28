package com.secureline.secureline.webrtc;

public class CodecManager {

    private OpusEncoderWrapper encoder;
    private OpusDecoderWrapper decoder;
    private int sampleRate;
    private int channels;

    public CodecManager(int sampleRate, int channels) {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.encoder = new OpusEncoderWrapper(sampleRate, channels);
        this.decoder = new OpusDecoderWrapper(sampleRate, channels);
    }

    public byte[] encode(short[] pcmData) {
        return encoder.encode(pcmData);
    }

    public short[] decode(byte[] encodedData) {
        return decoder.decode(encodedData, true);
    }

    public void setBitrate(int bitrate) {
        encoder.setBitrate(bitrate);
    }

    public int getBitrate() {
        return encoder.getBitrate();
    }

    public void setFecEnabled(boolean enabled) {
        encoder.setFecEnabled(enabled);
    }
}
