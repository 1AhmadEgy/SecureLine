package com.secureline.secureline.network;

import com.secureline.secureline.crypto.HashUtils;
import com.secureline.secureline.security.TokenGenerator;

public class ServerAuthenticator {

    private String authToken;
    private String userId;

    public ServerAuthenticator() {
        authToken = null;
        userId = null;
    }

    public boolean authenticate(String username, String password) {
        String passwordHash = HashUtils.sha256Hex(password.getBytes());
        String expectedHash = HashUtils.sha256Hex((username + password).getBytes());

        if (passwordHash != null && passwordHash.equals(expectedHash)) {
            userId = username;
            authToken = TokenGenerator.generateHexToken(32);
            return true;
        }
        return false;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isAuthenticated() {
        return authToken != null && userId != null;
    }

    public void logout() {
        authToken = null;
        userId = null;
    }
}
