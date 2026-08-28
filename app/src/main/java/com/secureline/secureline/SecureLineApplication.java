package com.secureline.secureline;

import android.app.Application;

import com.secureline.secureline.database.DatabaseManager;
import com.secureline.secureline.security.KeyManager;

public class SecureLineApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        KeyManager.getOrCreateDatabaseKey();
        DatabaseManager.getInstance(this);
    }
}
