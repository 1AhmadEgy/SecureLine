package com.secureline.secureline.webrtc;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.List;

public class WebRtcManager {

    private PeerConnectionFactory peerConnectionFactory;
    private PeerConnection peerConnection;
    private AudioSource audioSource;
    private AudioTrack audioTrack;
    private MediaStream mediaStream;
    private final List<IceCandidate> pendingIceCandidates;
    private WebRtcListener listener;

    public WebRtcManager() {
        pendingIceCandidates = new ArrayList<>();
    }

    public void initialize() {
        PeerConnectionFactory.InitializationOptions options = PeerConnectionFactory.InitializationOptions
            .builder(android.app.Application.getProcessName())
            .setEnableInternalTracer(false)
            .createInitializationOptions();

        PeerConnectionFactory.initialize(options);

        PeerConnectionFactory.Options factoryOptions = new PeerConnectionFactory.Options();
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(factoryOptions)
            .createPeerConnectionFactory();
    }

    public void createPeerConnection() {
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
        iceServers.add(PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer());

        PeerConnection.RTCConfiguration config = new PeerConnection.RTCConfiguration(iceServers);
        config.iceTransportsType = PeerConnection.IceTransportsType.ALL;
        config.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        config.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        config.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        config.keyType = PeerConnection.KeyType.ECDSA;

        peerConnection = peerConnectionFactory.createPeerConnection(config, new PeerConnection.Observer() {
            @Override
            public void onSignalingChange(PeerConnection.SignalingState signalingState) {}

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
                if (listener != null) {
                    listener.onIceConnectionStateChanged(iceConnectionState);
                }
            }

            @Override
            public void onIceConnectionReceivingChange(boolean receiving) {}

            @Override
            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {}

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                if (listener != null) {
                    listener.onIceCandidate(iceCandidate);
                }
            }

            @Override
            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {}

            @Override
            public void onAddStream(MediaStream stream) {}

            @Override
            public void onRemoveStream(MediaStream stream) {}

            @Override
            public void onDataChannel(org.webrtc.DataChannel dataChannel) {}

            @Override
            public void onRenegotiationNeeded() {}
        });
    }

    public void createAudioTrack() {
        MediaConstraints audioConstraints = MediaNegotiator.createAudioConstraints();
        audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
        audioTrack = peerConnectionFactory.createAudioTrack("secureline_audio", audioSource);

        mediaStream = peerConnectionFactory.createLocalMediaStream("secureline_stream");
        mediaStream.addTrack(audioTrack);
        peerConnection.addStream(mediaStream);
    }

    public void createOffer() {
        MediaConstraints constraints = MediaNegotiator.createAudioConstraints();
        peerConnection.createOffer(new org.webrtc.SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sdp) {
                peerConnection.setLocalDescription(new org.webrtc.SdpObserver() {
                    @Override
                    public void onCreateSuccess(SessionDescription sessionDescription) {}

                    @Override
                    public void onSetSuccess() {
                        if (listener != null) {
                            listener.onLocalDescriptionReady(sdp);
                        }
                    }

                    @Override
                    public void onCreateFailure(String s) {
                        if (listener != null) listener.onError("Set local description failed: " + s);
                    }

                    @Override
                    public void onSetFailure(String s) {
                        if (listener != null) listener.onError("Set local description failed: " + s);
                    }
                }, sdp);
            }

            @Override
            public void onSetSuccess() {}

            @Override
            public void onCreateFailure(String s) {
                if (listener != null) listener.onError("Create offer failed: " + s);
            }

            @Override
            public void onSetFailure(String s) {}
        }, constraints);
    }

    public void setRemoteDescription(SessionDescription sdp) {
        peerConnection.setRemoteDescription(new org.webrtc.SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {}

            @Override
            public void onSetSuccess() {
                flushPendingIceCandidates();
            }

            @Override
            public void onCreateFailure(String s) {}

            @Override
            public void onSetFailure(String s) {
                if (listener != null) listener.onError("Set remote description failed: " + s);
            }
        }, sdp);
    }

    public void addIceCandidate(IceCandidate candidate) {
        if (peerConnection == null) {
            pendingIceCandidates.add(candidate);
        } else {
            peerConnection.addIceCandidate(candidate);
        }
    }

    private void flushPendingIceCandidates() {
        for (IceCandidate candidate : pendingIceCandidates) {
            if (peerConnection != null) {
                peerConnection.addIceCandidate(candidate);
            }
        }
        pendingIceCandidates.clear();
    }

    public void close() {
        if (audioTrack != null) {
            audioTrack.dispose();
            audioTrack = null;
        }
        if (audioSource != null) {
            audioSource.dispose();
            audioSource = null;
        }
        if (peerConnection != null) {
            peerConnection.close();
            peerConnection.dispose();
            peerConnection = null;
        }
    }

    public void setListener(WebRtcListener listener) {
        this.listener = listener;
    }

    public interface WebRtcListener {
        void onLocalDescriptionReady(SessionDescription sdp);
        void onIceCandidate(IceCandidate candidate);
        void onIceConnectionStateChanged(PeerConnection.IceConnectionState state);
        void onError(String error);
    }
}
