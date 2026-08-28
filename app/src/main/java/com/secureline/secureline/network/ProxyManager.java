package com.secureline.secureline.network;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyManager {

    private final List<ProxyConfig> proxyConfigs;
    private int currentProxyIndex = -1;

    public ProxyManager() {
        proxyConfigs = new ArrayList<>();
    }

    public void addProxy(String host, int port, Proxy.Type type) {
        proxyConfigs.add(new ProxyConfig(host, port, type));
    }

    public void addTorProxy() {
        addProxy("127.0.0.1", 9050, Proxy.Type.SOCKS);
    }

    public Proxy getCurrentProxy() {
        if (currentProxyIndex < 0 || currentProxyIndex >= proxyConfigs.size()) {
            return null;
        }
        ProxyConfig config = proxyConfigs.get(currentProxyIndex);
        return new Proxy(config.type, new InetSocketAddress(config.host, config.port));
    }

    public void nextProxy() {
        if (proxyConfigs.isEmpty()) return;
        currentProxyIndex = (currentProxyIndex + 1) % proxyConfigs.size();
    }

    public void rotateProxy() {
        nextProxy();
    }

    public void disableProxy() {
        currentProxyIndex = -1;
    }

    public void enableProxy(int index) {
        if (index >= 0 && index < proxyConfigs.size()) {
            currentProxyIndex = index;
        }
    }

    private static class ProxyConfig {
        String host;
        int port;
        Proxy.Type type;

        ProxyConfig(String host, int port, Proxy.Type type) {
            this.host = host;
            this.port = port;
            this.type = type;
        }
    }
}
