package com.secureline.secureline.webrtc;

import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.List;

public class PeerConnectionManager {

    private PeerConnection peerConnection;
    private final List<IceCandidate> pendingCandidates;
    private final PeerConnection.Observer observer;

    public PeerConnectionManager(PeerConnection.Observer connectionObserver) {
        this.observer = connectionObserver;
        this.pendingCandidates = new ArrayList<>();
    }

    public void createPeerConnection(PeerConnectionFactoryManager factoryManager) {
        PeerConnection.RTCConfiguration config = new PeerConnection.RTCConfiguration(
            getIceServers()
        );
        config.iceTransportsType = PeerConnection.IceTransportsType.ALL;

        peerConnection = factoryManager.getFactory().createPeerConnection(config, observer);
    }

    private List<PeerConnection.IceServer> getIceServers() {
        List<PeerConnection.IceServer> servers = new ArrayList<>();
        servers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
        servers.add(PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer());
        return servers;
    }

    public void createOffer(SdpCallback callback) {
        MediaConstraints constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"));

        peerConnection.createOffer(new org.webrtc.SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sdp) {
                peerConnection.setLocalDescription(new org.webrtc.SdpObserver() {
                    @Override
                    public void onCreateSuccess(SessionDescription sessionDescription) {}
                    @Override
                    public void onSetSuccess() {
                        callback.onSdpReady(sdp);
                    }
                    @Override
                    public void onCreateFailure(String s) {}
                    @Override
                    public void onSetFailure(String s) {
                        callback.onError(s);
                    }
                }, sdp);
            }

            @Override
            public void onSetSuccess() {}

            @Override
            public void onCreateFailure(String s) {
                callback.onError(s);
            }

            @Override
            public void onSetFailure(String s) {}
        }, constraints);
    }

    public void setRemoteDescription(SessionDescription sdp, SdpCallback callback) {
        peerConnection.setRemoteDescription(new org.webrtc.SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {}
            @Override
            public void onSetSuccess() {
                callback.onSdpReady(null);
            }
            @Override
            public void onCreateFailure(String s) {}
            @Override
            public void onSetFailure(String s) {
                callback.onError(s);
            }
        }, sdp);
    }

    public void addIceCandidate(IceCandidate candidate) {
        if (peerConnection != null) {
            peerConnection.addIceCandidate(candidate);
        } else {
            pendingCandidates.add(candidate);
        }
    }

    public void close() {
        if (peerConnection != null) {
            peerConnection.close();
            peerConnection.dispose();
            peerConnection = null;
        }
    }

    public interface SdpCallback {
        void onSdpReady(SessionDescription sdp);
        void onError(String error);
    }
}

class PeerConnectionFactoryManager {
    private org.webrtc.PeerConnectionFactory factory;

    public org.webrtc.PeerConnectionFactory getFactory() {
        return factory;
    }
}
