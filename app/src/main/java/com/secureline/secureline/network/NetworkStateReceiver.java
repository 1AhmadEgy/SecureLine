package com.secureline.secureline.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class NetworkStateReceiver extends BroadcastReceiver {

    private NetworkStateListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return;

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) {
            if (listener != null) listener.onNetworkDisconnected();
            return;
        }

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            if (listener != null) listener.onNetworkConnected();
        } else {
            if (listener != null) listener.onNetworkDisconnected();
        }
    }

    public void setListener(NetworkStateListener listener) {
        this.listener = listener;
    }

    public interface NetworkStateListener {
        void onNetworkConnected();
        void onNetworkDisconnected();
    }
}
