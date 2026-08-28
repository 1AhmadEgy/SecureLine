package com.secureline.secureline.network;

import android.util.Log;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketManager {

    private static final String TAG = "SecureLine-WS";
    private WebSocket webSocket;
    private OkHttpClient client;
    private MessageListener messageListener;

    public WebSocketManager() {
        client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    }

    public void connect(String serverUrl) {
        Request request = new Request.Builder()
            .url(serverUrl)
            .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d(TAG, "Connected");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if (messageListener != null) {
                    messageListener.onTextMessage(text);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, java.nio.ByteBuffer bytes) {
                if (messageListener != null) {
                    messageListener.onBinaryMessage(bytes.array());
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e(TAG, "Connection failed: " + t.getMessage());
            }
        });
    }

    public void sendText(String text) {
        if (webSocket != null) {
            webSocket.send(text);
        }
    }

    public void sendBinary(byte[] data) {
        if (webSocket != null) {
            webSocket.send(okio.ByteString.of(data));
        }
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Normal closure");
        }
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public interface MessageListener {
        void onTextMessage(String text);
        void onBinaryMessage(byte[] data);
    }
}
