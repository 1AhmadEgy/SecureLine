package com.secureline.secureline.network;

import java.util.ArrayList;
import java.util.List;

public class TurnServerManager {

    private final List<TurnServerConfig> turnServers;

    public TurnServerManager() {
        turnServers = new ArrayList<>();
    }

    public void addTurnServer(String url, String username, String credential) {
        turnServers.add(new TurnServerConfig(url, username, credential));
    }

    public void addStunServer(String url) {
        turnServers.add(new TurnServerConfig(url, null, null));
    }

    public List<TurnServerConfig> getServers() {
        return turnServers;
    }

    public static class TurnServerConfig {
        private final String url;
        private final String username;
        private final String credential;

        public TurnServerConfig(String url, String username, String credential) {
            this.url = url;
            this.username = username;
            this.credential = credential;
        }

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }

        public String getCredential() {
            return credential;
        }

        public boolean isStun() {
            return username == null && credential == null;
        }
    }
}
