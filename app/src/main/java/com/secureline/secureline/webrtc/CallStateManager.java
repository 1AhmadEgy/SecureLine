package com.secureline.secureline.webrtc;

public class CallStateManager {

    private CallState currentState;
    private CallStateListener listener;

    public enum CallState {
        IDLE,
        RINGING,
        CONNECTING,
        CONNECTED,
        HOLD,
        ENDING,
        ENDED,
        FAILED
    }

    public CallStateManager() {
        currentState = CallState.IDLE;
    }

    public CallState getCurrentState() {
        return currentState;
    }

    public void setState(CallState newState) {
        CallState oldState = currentState;
        currentState = newState;
        if (listener != null) {
            listener.onStateChanged(oldState, newState);
        }
    }

    public boolean isInCall() {
        return currentState == CallState.CONNECTING ||
               currentState == CallState.CONNECTED ||
               currentState == CallState.RINGING;
    }

    public boolean isIdle() {
        return currentState == CallState.IDLE;
    }

    public void reset() {
        currentState = CallState.IDLE;
    }

    public void setListener(CallStateListener listener) {
        this.listener = listener;
    }

    public interface CallStateListener {
        void onStateChanged(CallState oldState, CallState newState);
    }
}
