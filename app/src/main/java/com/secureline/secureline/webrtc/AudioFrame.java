package com.secureline.secureline.webrtc;

public class AudioFrame {

    private final short[] pcmSamples;
    private final int sampleRate;
    private final int channels;
    private final long timestamp;

    public AudioFrame(short[] samples, int sampleRate, int channels, long timestamp) {
        this.pcmSamples = samples;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.timestamp = timestamp;
    }

    public short[] getPcmSamples() {
        return pcmSamples;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getFrameSize() {
        return pcmSamples.length;
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[pcmSamples.length * 2];
        for (int i = 0; i < pcmSamples.length; i++) {
            bytes[i * 2] = (byte) ((pcmSamples[i] >> 8) & 0xFF);
            bytes[i * 2 + 1] = (byte) (pcmSamples[i] & 0xFF);
        }
        return bytes;
    }

    public static AudioFrame fromBytes(byte[] bytes, int sampleRate, int channels, long timestamp) {
        short[] samples = new short[bytes.length / 2];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = (short) (((bytes[i * 2] & 0xFF) << 8) | (bytes[i * 2 + 1] & 0xFF));
        }
        return new AudioFrame(samples, sampleRate, channels, timestamp);
    }
}
