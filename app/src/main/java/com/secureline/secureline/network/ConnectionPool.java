package com.secureline.secureline.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionPool {

    private final Map<String, Object> connections;

    public ConnectionPool() {
        connections = new ConcurrentHashMap<>();
    }

    public void addConnection(String connectionId, Object connection) {
        connections.put(connectionId, connection);
    }

    public Object getConnection(String connectionId) {
        return connections.get(connectionId);
    }

    public void removeConnection(String connectionId) {
        connections.remove(connectionId);
    }

    public boolean hasConnection(String connectionId) {
        return connections.containsKey(connectionId);
    }

    public void clearAllConnections() {
        connections.clear();
    }

    public int getConnectionCount() {
        return connections.size();
    }
}
