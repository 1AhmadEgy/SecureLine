package com.secureline.secureline.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnectionPool {

    private final Map<String, ServerConnectionManager> connections;

    public ServerConnectionPool() {
        connections = new ConcurrentHashMap<>();
    }

    public void addConnection(String connectionId, ServerConnectionManager connection) {
        connections.put(connectionId, connection);
    }

    public ServerConnectionManager getConnection(String connectionId) {
        return connections.get(connectionId);
    }

    public void removeConnection(String connectionId) {
        connections.remove(connectionId);
    }

    public void disconnectAll() {
        for (ServerConnectionManager connection : connections.values()) {
            connection.disconnect();
        }
        connections.clear();
    }

    public int getConnectionCount() {
        return connections.size();
    }
}
