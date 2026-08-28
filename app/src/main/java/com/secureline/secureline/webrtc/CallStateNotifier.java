package com.secureline.secureline.webrtc;

import java.util.ArrayList;
import java.util.List;

public class CallStateNotifier {

    private final List<CallStateListener> listeners;

    public CallStateNotifier() {
        listeners = new ArrayList<>();
    }

    public void addListener(CallStateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(CallStateListener listener) {
        listeners.remove(listener);
    }

    public void notifyStateChanged(CallSession.CallState oldState, CallSession.CallState newState) {
        for (CallStateListener listener : listeners) {
            listener.onStateChanged(oldState, newState);
        }
    }

    public void notifyCallStarted(String sessionId) {
        for (CallStateListener listener : listeners) {
            listener.onCallStarted(sessionId);
        }
    }

    public void notifyCallEnded(String sessionId) {
        for (CallStateListener listener : listeners) {
            listener.onCallEnded(sessionId);
        }
    }

    public void clearListeners() {
        listeners.clear();
    }

    public interface CallStateListener {
        void onStateChanged(CallSession.CallState oldState, CallSession.CallState newState);
        void onCallStarted(String sessionId);
        void onCallEnded(String sessionId);
    }
}
