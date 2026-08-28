package com.secureline.secureline.webrtc;

import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.List;

public class ICEAgent {

    private final List<IceCandidate> localCandidates;
    private final List<IceCandidate> remoteCandidates;

    public ICEAgent() {
        localCandidates = new ArrayList<>();
        remoteCandidates = new ArrayList<>();
    }

    public void addLocalCandidate(IceCandidate candidate) {
        localCandidates.add(candidate);
    }

    public void addRemoteCandidate(IceCandidate candidate) {
        remoteCandidates.add(candidate);
    }

    public void addRemoteCandidate(String sdpMid, int sdpMLineIndex, String sdp) {
        IceCandidate candidate = new IceCandidate(sdpMid, sdpMLineIndex, sdp);
        remoteCandidates.add(candidate);
    }

    public List<IceCandidate> getLocalCandidates() {
        return localCandidates;
    }

    public List<IceCandidate> getRemoteCandidates() {
        return remoteCandidates;
    }

    public void clearCandidates() {
        localCandidates.clear();
        remoteCandidates.clear();
    }
}
