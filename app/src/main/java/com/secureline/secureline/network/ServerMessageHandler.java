package com.secureline.secureline.network;

public class ServerMessageHandler {

    private MessageHandlerListener listener;

    public void handleIncomingMessage(byte[] data) {
        if (listener != null) {
            listener.onMessageReceived(data);
        }
    }

    public void handleServerResponse(byte[] data) {
        if (listener != null) {
            listener.onServerResponse(data);
        }
    }

    public void handleError(int errorCode, String errorMessage) {
        if (listener != null) {
            listener.onError(errorCode, errorMessage);
        }
    }

    public void setListener(MessageHandlerListener listener) {
        this.listener = listener;
    }

    public interface MessageHandlerListener {
        void onMessageReceived(byte[] data);
        void onServerResponse(byte[] data);
        void onError(int errorCode, String errorMessage);
    }
}
