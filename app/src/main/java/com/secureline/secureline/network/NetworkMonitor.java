package com.secureline.secureline.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkMonitor {

    private final Context context;

    public NetworkMonitor(Context context) {
        this.context = context;
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public String getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return "UNKNOWN";
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return "DISCONNECTED";
        return info.getTypeName();
    }

    public boolean isOnWifi() {
        return "WIFI".equals(getNetworkType());
    }

    public boolean isOnMobileData() {
        return "MOBILE".equals(getNetworkType());
    }
}
