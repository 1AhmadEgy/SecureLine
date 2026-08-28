package com.secureline.secureline.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBroker {

    private final Map<String, MessageListener> listeners;

    public MessageBroker() {
        listeners = new ConcurrentHashMap<>();
    }

    public void subscribe(String topic, MessageListener listener) {
        listeners.put(topic, listener);
    }

    public void unsubscribe(String topic) {
        listeners.remove(topic);
    }

    public void publish(String topic, byte[] message) {
        MessageListener listener = listeners.get(topic);
        if (listener != null) {
            listener.onMessage(message);
        }
    }

    public void publishToAll(byte[] message) {
        for (MessageListener listener : listeners.values()) {
            listener.onMessage(message);
        }
    }

    public interface MessageListener {
        void onMessage(byte[] message);
    }
}
