package com.secureline.secureline.webrtc;

import java.util.UUID;

public class CallSession {

    private final String sessionId;
    private final String remoteUserId;
    private long startTime;
    private long endTime;
    private CallState state;

    public enum CallState {
        RINGING,
        CONNECTED,
        ENDED,
        FAILED
    }

    public CallSession(String remoteUserId) {
        this.sessionId = UUID.randomUUID().toString();
        this.remoteUserId = remoteUserId;
        this.state = CallState.RINGING;
        this.startTime = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getRemoteUserId() {
        return remoteUserId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public CallState getState() {
        return state;
    }

    public void setConnected() {
        this.state = CallState.CONNECTED;
        this.startTime = System.currentTimeMillis();
    }

    public void setEnded() {
        this.state = CallState.ENDED;
        this.endTime = System.currentTimeMillis();
    }

    public void setFailed() {
        this.state = CallState.FAILED;
        this.endTime = System.currentTimeMillis();
    }

    public long getDurationSeconds() {
        if (state == CallState.CONNECTED) {
            return (System.currentTimeMillis() - startTime) / 1000;
        }
        if (endTime > startTime) {
            return (endTime - startTime) / 1000;
        }
        return 0;
    }
}
