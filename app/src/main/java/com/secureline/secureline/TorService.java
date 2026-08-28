package com.secureline.secureline;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.secureline.secureline.network.TorManager;

public class TorService extends Service {

    private TorManager torManager;

    @Override
    public void onCreate() {
        super.onCreate();
        torManager = new TorManager();
        torManager.start(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (torManager != null) {
            torManager.stop();
        }
        super.onDestroy();
    }
}
