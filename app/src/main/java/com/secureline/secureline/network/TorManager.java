package com.secureline.secureline.network;

import android.content.Context;
import android.util.Log;

import org.torproject.jni.TorService;

public class TorManager {

    private static final String TAG = "SecureLine-Tor";
    private TorService torService;
    private boolean isRunning = false;

    public void start(Context context) {
        if (isRunning) {
            Log.d(TAG, "Tor already running");
            return;
        }

        try {
            torService = new TorService(context);
            torService.start();
            isRunning = true;
            Log.d(TAG, "Tor started successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to start Tor: " + e.getMessage());
            isRunning = false;
        }
    }

    public void stop() {
        if (torService != null && isRunning) {
            try {
                torService.stop();
                isRunning = false;
                Log.d(TAG, "Tor stopped");
            } catch (Exception e) {
                Log.e(TAG, "Failed to stop Tor: " + e.getMessage());
            }
        }
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
