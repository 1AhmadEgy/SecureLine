package com.secureline.secureline;

import android.app.Application;
import com.secureline.secureline.network.ServerConnectionManager;
import com.secureline.secureline.security.ScreenshotProtection;

public class SecureLineApplication extends Application {
    
    private static SecureLineApplication instance;
    private ServerConnectionManager connectionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // تهيئة مدير الاتصال بالخادم عند تشغيل التطبيق
        connectionManager = new ServerConnectionManager();
    }

    public static SecureLineApplication getInstance() {
        return instance;
    }

    public ServerConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
