package com.secureline.secureline.network;

public class NetworkConfig {

    private String serverUrl;
    private String stunServer;
    private String turnServer;
    private int connectionTimeoutMs;
    private int readTimeoutMs;
    private int writeTimeoutMs;
    private boolean torEnabled;

    public NetworkConfig() {
        serverUrl = "https://localhost:8080";
        stunServer = "stun:stun.l.google.com:19302";
        turnServer = "";
        connectionTimeoutMs = 10000;
        readTimeoutMs = 30000;
        writeTimeoutMs = 30000;
        torEnabled = false;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getStunServer() {
        return stunServer;
    }

    public void setStunServer(String stunServer) {
        this.stunServer = stunServer;
    }

    public String getTurnServer() {
        return turnServer;
    }

    public void setTurnServer(String turnServer) {
        this.turnServer = turnServer;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public int getWriteTimeoutMs() {
        return writeTimeoutMs;
    }

    public void setWriteTimeoutMs(int writeTimeoutMs) {
        this.writeTimeoutMs = writeTimeoutMs;
    }

    public boolean isTorEnabled() {
        return torEnabled;
    }

    public void setTorEnabled(boolean torEnabled) {
        this.torEnabled = torEnabled;
    }
}
