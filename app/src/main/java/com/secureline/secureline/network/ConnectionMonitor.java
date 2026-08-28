package com.secureline.secureline.network;

public class ConnectionMonitor {

    private ConnectionState currentState;
    private ConnectionListener listener;
    private long lastPingTime;
    private long lastPongTime;

    public ConnectionMonitor() {
        currentState = ConnectionState.DISCONNECTED;
        lastPingTime = 0;
        lastPongTime = 0;
    }

    public void setState(ConnectionState state) {
        ConnectionState oldState = currentState;
        currentState = state;
        if (listener != null) {
            listener.onStateChanged(oldState, state);
        }
    }

    public ConnectionState getCurrentState() {
        return currentState;
    }

    public void sendPing() {
        lastPingTime = System.currentTimeMillis();
    }

    public void receivePong() {
        lastPongTime = System.currentTimeMillis();
    }

    public long getLatency() {
        if (lastPingTime == 0 || lastPongTime == 0) return -1;
        return lastPongTime - lastPingTime;
    }

    public boolean isConnectionHealthy() {
        long latency = getLatency();
        return latency >= 0 && latency < 1000;
    }

    public void setListener(ConnectionListener listener) {
        this.listener = listener;
    }

    public interface ConnectionListener {
        void onStateChanged(ConnectionState oldState, ConnectionState newState);
    }
}
