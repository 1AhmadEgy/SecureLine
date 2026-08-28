package com.secureline.secureline.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class NetworkManager {

    private final Context context;
    private ConnectionState currentState;

    public NetworkManager(Context context) {
        this.context = context;
        this.currentState = ConnectionState.DISCONNECTED;
    }

    public ConnectionState getCurrentState() {
        return currentState;
    }

    public void setState(ConnectionState state) {
        this.currentState = state;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        Network network = cm.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (capabilities == null) return false;
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    public void registerNetworkCallback(ConnectivityManager.NetworkCallback callback) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            cm.registerDefaultNetworkCallback(callback);
        }
    }
}
