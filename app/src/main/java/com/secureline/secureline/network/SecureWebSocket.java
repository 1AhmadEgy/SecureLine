package com.secureline.secureline.network;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SecureWebSocket {

    private WebSocket webSocket;
    private OkHttpClient client;
    private SecureWebSocketListener listener;

    public SecureWebSocket() {
        buildSecureClient();
    }

    private void buildSecureClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm()
            );
            trustManagerFactory.init((java.security.KeyStore) null);

            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), 
                    new javax.net.ssl.X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
                    })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        } catch (Exception e) {
            client = new OkHttpClient();
        }
    }

    public void connect(String url) {
        Request request = new Request.Builder()
            .url(url)
            .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                if (listener != null) listener.onConnected();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if (listener != null) listener.onMessage(text);
            }

            @Override
            public void onMessage(WebSocket webSocket, java.nio.ByteBuffer bytes) {
                if (listener != null) listener.onBinaryMessage(bytes.array());
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                if (listener != null) listener.onDisconnected();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                if (listener != null) listener.onError(t.getMessage());
            }
        });
    }

    public void send(byte[] data) {
        if (webSocket != null) {
            webSocket.send(okio.ByteString.of(data));
        }
    }

    public void send(String text) {
        if (webSocket != null) {
            webSocket.send(text);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Normal");
            webSocket = null;
        }
    }

    public void setListener(SecureWebSocketListener listener) {
        this.listener = listener;
    }

    public interface SecureWebSocketListener {
        void onConnected();
        void onDisconnected();
        void onMessage(String text);
        void onBinaryMessage(byte[] data);
        void onError(String error);
    }
}
