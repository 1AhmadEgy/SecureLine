package com.secureline.server;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientConnectionManager {

    private final Map<String, Socket> clientConnections;

    public ClientConnectionManager() {
        clientConnections = new ConcurrentHashMap<>();
    }

    public void registerClient(String clientId, Socket socket) {
        clientConnections.put(clientId, socket);
    }

    public void unregisterClient(String clientId) {
        clientConnections.remove(clientId);
    }

    public Socket getClientConnection(String clientId) {
        return clientConnections.get(clientId);
    }

    public boolean isClientConnected(String clientId) {
        Socket socket = clientConnections.get(clientId);
        return socket != null && socket.isConnected();
    }

    public int getConnectedClientCount() {
        return clientConnections.size();
    }

    public void disconnectAllClients() {
        for (Socket socket : clientConnections.values()) {
            try {
                socket.close();
            } catch (Exception e) {
                // Ignore
            }
        }
        clientConnections.clear();
    }
}
