package com.secureline.secureline.network;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.List;

public class IceCandidateManager {

    private final List<IceCandidate> candidates;
    private SessionDescription localDescription;
    private SessionDescription remoteDescription;

    public IceCandidateManager() {
        candidates = new ArrayList<>();
    }

    public void addCandidate(IceCandidate candidate) {
        candidates.add(candidate);
    }

    public void addCandidate(String sdpMid, int sdpMLineIndex, String sdp) {
        candidates.add(new IceCandidate(sdpMid, sdpMLineIndex, sdp));
    }

    public List<IceCandidate> getCandidates() {
        return candidates;
    }

    public void setLocalDescription(SessionDescription description) {
        this.localDescription = description;
    }

    public void setRemoteDescription(SessionDescription description) {
        this.remoteDescription = description;
    }

    public SessionDescription getLocalDescription() {
        return localDescription;
    }

    public SessionDescription getRemoteDescription() {
        return remoteDescription;
    }

    public void clear() {
        candidates.clear();
        localDescription = null;
        remoteDescription = null;
    }
}
