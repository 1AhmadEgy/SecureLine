package com.secureline.secureline.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.torproject.jni.TorService;

public class TorManager {

    private static final String TAG = "SecureLine-Tor";
    private Context context;
    private boolean isRunning = false;

    public void start(Context context) {
        if (isRunning) {
            Log.d(TAG, "Tor already running");
            return;
        }

        this.context = context.getApplicationContext();

        try {
            Intent intent = new Intent(this.context, TorService.class);
            this.context.startService(intent);
            isRunning = true;
            Log.d(TAG, "Tor start requested");
        } catch (Exception e) {
            isRunning = false;
            Log.e(TAG, "Failed to start Tor", e);
        }
    }

    public void stop() {
        if (context != null) {
            try {
                context.stopService(new Intent(context, TorService.class));
            } catch (Exception e) {
                Log.e(TAG, "Failed to stop Tor", e);
            }
        }
        isRunning = false;
    }

    public boolean isTorRunning() {
        return isRunning;
    }

    public String getProxyHost() {
        return "127.0.0.1";
    }

    public int getProxyPort() {
        return 9050;
    }
}
