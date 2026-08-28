package com.secureline.secureline.webrtc;

import java.util.HashMap;
import java.util.Map;

public class CallManager {

    private final Map<String, CallSession> activeCalls;
    private static CallManager instance;

    private CallManager() {
        activeCalls = new HashMap<>();
    }

    public static synchronized CallManager getInstance() {
        if (instance == null) {
            instance = new CallManager();
        }
        return instance;
    }

    public CallSession startCall(String remoteUserId) {
        CallSession session = new CallSession(remoteUserId);
        activeCalls.put(session.getSessionId(), session);
        return session;
    }

    public void endCall(String sessionId) {
        CallSession session = activeCalls.get(sessionId);
        if (session != null) {
            session.setEnded();
            activeCalls.remove(sessionId);
        }
    }

    public CallSession getCall(String sessionId) {
        return activeCalls.get(sessionId);
    }

    public boolean isInCall() {
        for (CallSession session : activeCalls.values()) {
            if (session.getState() == CallSession.CallState.CONNECTED ||
                session.getState() == CallSession.CallState.RINGING) {
                return true;
            }
        }
        return false;
    }

    public int getActiveCallCount() {
        return activeCalls.size();
    }

    public void endAllCalls() {
        for (CallSession session : activeCalls.values()) {
            session.setEnded();
        }
        activeCalls.clear();
    }
}
