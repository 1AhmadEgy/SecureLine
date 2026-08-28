package com.secureline.secureline.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = cm.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
    }

    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return "UNKNOWN";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = cm.getActiveNetwork();
            if (network == null) return "DISCONNECTED";
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            if (capabilities == null) return "UNKNOWN";

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return "WIFI";
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return "MOBILE";
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return "ETHERNET";
            return "UNKNOWN";
        } else {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) return "DISCONNECTED";
            return info.getTypeName();
        }
    }
}
