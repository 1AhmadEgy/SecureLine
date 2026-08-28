package com.secureline.secureline.webrtc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallSessionManager {

    private final Map<String, CallSession> sessions;
    private static CallSessionManager instance;

    private CallSessionManager() {
        sessions = new ConcurrentHashMap<>();
    }

    public static synchronized CallSessionManager getInstance() {
        if (instance == null) {
            instance = new CallSessionManager();
        }
        return instance;
    }

    public CallSession createSession(String remoteUserId) {
        CallSession session = new CallSession(remoteUserId);
        sessions.put(session.getSessionId(), session);
        return session;
    }

    public CallSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void endSession(String sessionId) {
        CallSession session = sessions.remove(sessionId);
        if (session != null) {
            session.setEnded();
        }
    }

    public void endAllSessions() {
        for (CallSession session : sessions.values()) {
            session.setEnded();
        }
        sessions.clear();
    }

    public boolean hasActiveSession() {
        for (CallSession session : sessions.values()) {
            if (session.getState() == CallSession.CallState.CONNECTED ||
                session.getState() == CallSession.CallState.RINGING) {
                return true;
            }
        }
        return false;
    }

    public int getSessionCount() {
        return sessions.size();
    }
}
