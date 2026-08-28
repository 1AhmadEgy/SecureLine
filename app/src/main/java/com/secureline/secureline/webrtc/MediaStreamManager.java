package com.secureline.secureline.webrtc;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;

public class MediaStreamManager {

    private final PeerConnectionFactory factory;
    private AudioSource audioSource;
    private AudioTrack audioTrack;

    public MediaStreamManager(PeerConnectionFactory peerFactory) {
        this.factory = peerFactory;
    }

    public AudioTrack createAudioTrack(String trackId) {
        MediaConstraints constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation", "true"));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression", "true"));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl", "true"));

        audioSource = factory.createAudioSource(constraints);
        audioTrack = factory.createAudioTrack(trackId, audioSource);
        return audioTrack;
    }

    public void dispose() {
        if (audioTrack != null) {
            audioTrack.dispose();
        }
        if (audioSource != null) {
            audioSource.dispose();
        }
    }
}
