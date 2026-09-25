package com.secureline.secureline.network;

import com.secureline.secureline.BuildConfig;

public final class NetworkConfig {
    private NetworkConfig() {}

    public static final String SERVER_IP = BuildConfig.SECURELINE_SERVER_HOST;
    public static final int SERVER_PORT = BuildConfig.SECURELINE_SERVER_PORT;
    public static final int CONNECTION_TIMEOUT = 10_000;
}
