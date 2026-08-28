package com.secureline.secureline.network;

public class NetworkErrorHandler {

    public static void handleNetworkError(int errorCode, ErrorListener listener) {
        String message;
        switch (errorCode) {
            case 401:
                message = "Authentication failed";
                break;
            case 403:
                message = "Access denied";
                break;
            case 404:
                message = "Resource not found";
                break;
            case 408:
                message = "Request timeout";
                break;
            case 429:
                message = "Too many requests";
                break;
            case 500:
                message = "Server error";
                break;
            case 503:
                message = "Service unavailable";
                break;
            default:
                message = "Unknown network error";
                break;
        }
        if (listener != null) {
            listener.onError(errorCode, message);
        }
    }

    public interface ErrorListener {
        void onError(int errorCode, String message);
    }
}
