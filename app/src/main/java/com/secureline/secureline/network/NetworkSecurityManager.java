package com.secureline.secureline.network;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class NetworkSecurityManager {

    private Proxy proxy;
    private boolean torEnabled;
    private boolean vpnEnabled;

    public NetworkSecurityManager() {
        proxy = null;
        torEnabled = false;
        vpnEnabled = false;
    }

    public void enableTor() {
        torEnabled = true;
        proxy = new Proxy(Proxy.Type.SOCKS, 
            new InetSocketAddress("127.0.0.1", 9050));
    }

    public void disableTor() {
        torEnabled = false;
        if (!vpnEnabled) {
            proxy = null;
        }
    }

    public void enableVpn(String host, int port) {
        vpnEnabled = true;
        proxy = new Proxy(Proxy.Type.SOCKS, 
            new InetSocketAddress(host, port));
    }

    public void disableVpn() {
        vpnEnabled = false;
        if (!torEnabled) {
            proxy = null;
        }
    }

    public Proxy getProxy() {
        return proxy;
    }

    public boolean isProxyEnabled() {
        return proxy != null;
    }

    public boolean isTorEnabled() {
        return torEnabled;
    }
}
