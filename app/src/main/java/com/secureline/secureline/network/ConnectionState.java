package com.secureline.secureline.network;

public enum ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING,
    FAILED,
    TOR_CONNECTING,
    TOR_CONNECTED,
    TOR_FAILED
}
