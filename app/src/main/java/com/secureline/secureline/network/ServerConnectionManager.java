package com.secureline.secureline.network;

public class ServerConnectionManager {

    private final NetworkConfig config;
    private ConnectionMonitor connectionMonitor;
    private WebSocketManager webSocketManager;
    private boolean isConnected;

    public ServerConnectionManager(NetworkConfig config) {
        this.config = config;
        this.connectionMonitor = new ConnectionMonitor();
        this.isConnected = false;
    }

    public void connect() {
        connectionMonitor.setState(ConnectionState.CONNECTING);
        webSocketManager = new WebSocketManager();
        webSocketManager.setMessageListener(new WebSocketManager.MessageListener() {
            @Override
            public void onTextMessage(String text) {
                // Handle text message
            }

            @Override
            public void onBinaryMessage(byte[] data) {
                // Handle binary message
            }
        });
        webSocketManager.connect(config.getServerUrl());
        isConnected = true;
        connectionMonitor.setState(ConnectionState.CONNECTED);
    }

    public void disconnect() {
        if (webSocketManager != null) {
            webSocketManager.disconnect();
        }
        isConnected = false;
        connectionMonitor.setState(ConnectionState.DISCONNECTED);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public ConnectionMonitor getConnectionMonitor() {
        return connectionMonitor;
    }

    public void sendMessage(String recipientId, byte[] encryptedData) {
        if (webSocketManager != null && isConnected) {
            webSocketManager.sendBinary(encryptedData);
        }
    }
}
