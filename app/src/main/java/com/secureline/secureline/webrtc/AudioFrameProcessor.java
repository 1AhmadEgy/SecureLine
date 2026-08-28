package com.secureline.secureline.webrtc;

public class AudioFrameProcessor {

    private final EchoCanceller echoCanceller;
    private final AutoGainControl autoGainControl;
    private final VoiceActivityDetector voiceActivityDetector;
    private final NoiseGate noiseGate;

    public AudioFrameProcessor() {
        echoCanceller = new EchoCanceller(1024);
        autoGainControl = new AutoGainControl();
        voiceActivityDetector = new VoiceActivityDetector(100, 50);
        noiseGate = new NoiseGate(500);
    }

    public short[] processFrame(short[] input, short[] reference) {
        short[] echoCancelled = echoCanceller.process(input, reference);
        short[] gainControlled = autoGainControl.process(echoCancelled);

        boolean isVoice = voiceActivityDetector.detectVoice(gainControlled);
        if (!isVoice) {
            return new short[gainControlled.length];
        }

        return gainControlled;
    }

    public boolean isVoiceActive() {
        return voiceActivityDetector.isVoiceDetected();
    }
}
