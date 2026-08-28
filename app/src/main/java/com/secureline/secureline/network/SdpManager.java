package com.secureline.secureline.network;

import org.webrtc.SessionDescription;

public class SdpManager {

    private SessionDescription localSdp;
    private SessionDescription remoteSdp;

    public void setLocalSdp(SessionDescription sdp) {
        this.localSdp = sdp;
    }

    public void setRemoteSdp(SessionDescription sdp) {
        this.remoteSdp = sdp;
    }

    public SessionDescription getLocalSdp() {
        return localSdp;
    }

    public SessionDescription getRemoteSdp() {
        return remoteSdp;
    }

    public boolean isNegotiationComplete() {
        return localSdp != null && remoteSdp != null;
    }

    public void clear() {
        localSdp = null;
        remoteSdp = null;
    }
}
