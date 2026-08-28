package com.secureline.secureline.webrtc;

public class OpusDecoderWrapper {

    private int sampleRate;
    private int channels;
    private int frameSize;

    public OpusDecoderWrapper(int sampleRate, int channels) {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.frameSize = sampleRate / 50;
    }

    public short[] decode(byte[] compressedData, boolean fec) {
        if (compressedData == null || compressedData.length == 0) {
            return new short[frameSize * channels];
        }

        short[] pcmSamples = new short[compressedData.length / 2];
        for (int i = 0; i < pcmSamples.length; i++) {
            pcmSamples[i] = (short) (((compressedData[i * 2] & 0xFF) << 8) |
                                     (compressedData[i * 2 + 1] & 0xFF));
        }
        return pcmSamples;
    }

    public int getFrameSize() {
        return frameSize;
    }
}
