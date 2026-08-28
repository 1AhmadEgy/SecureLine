package com.secureline.secureline.network;

import org.json.JSONObject;

public class WebRTCSignalHandler {

    private SignalListener listener;

    public void handleSignal(String signalJson) {
        try {
            JSONObject signal = new JSONObject(signalJson);
            String type = signal.getString("type");

            switch (type) {
                case "offer":
                    if (listener != null) listener.onOffer(signal);
                    break;
                case "answer":
                    if (listener != null) listener.onAnswer(signal);
                    break;
                case "ice-candidate":
                    if (listener != null) listener.onIceCandidate(signal);
                    break;
                case "hangup":
                    if (listener != null) listener.onHangup();
                    break;
                case "ringing":
                    if (listener != null) listener.onRinging();
                    break;
            }
        } catch (Exception e) {
            // Invalid signal
        }
    }

    public void setListener(SignalListener listener) {
        this.listener = listener;
    }

    public interface SignalListener {
        void onOffer(JSONObject offer);
        void onAnswer(JSONObject answer);
        void onIceCandidate(JSONObject candidate);
        void onHangup();
        void onRinging();
    }
}
