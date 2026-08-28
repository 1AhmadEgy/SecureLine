package com.secureline.secureline.webrtc;

import org.json.JSONObject;

public class SignalingClient {

    private SignalingListener listener;

    public void sendOffer(String sessionId, String sdp) {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "offer");
            message.put("sessionId", sessionId);
            message.put("sdp", sdp);
            if (listener != null) {
                listener.onSignalOutgoing(message);
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    public void sendAnswer(String sessionId, String sdp) {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "answer");
            message.put("sessionId", sessionId);
            message.put("sdp", sdp);
            if (listener != null) {
                listener.onSignalOutgoing(message);
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    public void sendIceCandidate(String sessionId, String sdpMid, int sdpMLineIndex, String sdp) {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "ice-candidate");
            message.put("sessionId", sessionId);
            message.put("sdpMid", sdpMid);
            message.put("sdpMLineIndex", sdpMLineIndex);
            message.put("sdp", sdp);
            if (listener != null) {
                listener.onSignalOutgoing(message);
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    public void sendHangup(String sessionId) {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "hangup");
            message.put("sessionId", sessionId);
            if (listener != null) {
                listener.onSignalOutgoing(message);
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    public void handleIncomingSignal(JSONObject signal) {
        if (listener != null) {
            listener.onSignalIncoming(signal);
        }
    }

    public void setListener(SignalingListener listener) {
        this.listener = listener;
    }

    public interface SignalingListener {
        void onSignalOutgoing(JSONObject signal);
        void onSignalIncoming(JSONObject signal);
    }
}
