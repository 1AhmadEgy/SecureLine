package com.secureline.secureline.network;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerConnectionManager {
    private static final String TAG = "ServerConnection";
    private Socket socket;
    private BufferedReader reader;
    private OutputStream writer;
    private boolean isConnected = false;
    private ServerMessageListener listener;

    public interface ServerMessageListener {
        void onConnected();
        void onDisconnected();
        void onMessageReceived(String message);
        void onError(Exception e);
    }

    public void setListener(ServerMessageListener listener) {
        this.listener = listener;
    }

    public void connect() {
        new Thread(() -> {
            try {
                socket = new Socket();
                // الاتصال بالخادم باستخدام IP والمنفذ المحددين
                socket.connect(new InetSocketAddress(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT), NetworkConfig.CONNECTION_TIMEOUT);
                
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                writer = socket.getOutputStream();
                isConnected = true;

                if (listener != null) {
                    listener.onConnected();
                }

                listenForMessages();
            } catch (Exception e) {
                isConnected = false;
                if (listener != null) {
                    listener.onError(e);
                }
            }
        }).start();
    }

    private void listenForMessages() {
        try {
            String line;
            while (isConnected && (line = reader.readLine()) != null) {
                if (listener != null) {
                    listener.onMessageReceived(line);
                }
            }
        } catch (Exception e) {
            if (isConnected && listener != null) {
                listener.onError(e);
            }
        } finally {
            disconnect();
        }
    }

    public void sendMessage(String message) {
        if (!isConnected || writer == null) return;
        
        new Thread(() -> {
            try {
                writer.write((message + "\n").getBytes(StandardCharsets.UTF_8));
                writer.flush();
            } catch (Exception e) {
                Log.e(TAG, "Failed to send message", e);
            }
        }).start();
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            Log.e(TAG, "Error closing connection", e);
        }
        if (listener != null) {
            listener.onDisconnected();
        }
    }
    
    public boolean isConnected() {
        return isConnected;
    }
}
