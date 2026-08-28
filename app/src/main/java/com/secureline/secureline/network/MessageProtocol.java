package com.secureline.secureline.network;

import org.json.JSONObject;

public class MessageProtocol {
    
    public static String createAuthMessage(String username, String passwordHash) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "AUTH");
            json.put("username", username);
            json.put("password_hash", passwordHash);
            return json.toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    public static String createChatMessage(String recipient, String encryptedBody) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "CHAT");
            json.put("recipient", recipient);
            json.put("body", encryptedBody);
            return json.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
    
    public static String createPingMessage() {
        return "{\"type\":\"PING\"}";
    }
}
