package com.secureline.secureline.network;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServerApiClient {

    private final String serverUrl;
    private final String authToken;

    public ServerApiClient(String serverUrl, String authToken) {
        this.serverUrl = serverUrl;
        this.authToken = authToken;
    }

    public boolean sendMessage(String recipientId, byte[] encryptedBody) {
        try {
            URL url = new URL(serverUrl + "/v1/messages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
            connection.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("recipient_id", recipientId);
            body.put("encrypted_body", Base64.encodeToString(encryptedBody, Base64.NO_WRAP));
            body.put("timestamp", System.currentTimeMillis());

            OutputStream output = connection.getOutputStream();
            output.write(body.toString().getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();

            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public JSONArray fetchMessages() {
        try {
            URL url = new URL(serverUrl + "/v1/messages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);

            if (connection.getResponseCode() != 200) return null;

            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(connection.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getJSONArray("messages");
        } catch (Exception e) {
            return null;
        }
    }

    public boolean uploadPublicKey(byte[] publicKey) {
        try {
            URL url = new URL(serverUrl + "/v1/keys");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
            connection.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("public_key", Base64.encodeToString(publicKey, Base64.NO_WRAP));

            OutputStream output = connection.getOutputStream();
            output.write(body.toString().getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();

            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
