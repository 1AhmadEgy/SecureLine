package com.secureline.secureline.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventHandler {

    private final Map<String, CopyOnWriteArrayList<EventListener>> listeners;

    public EventHandler() {
        listeners = new ConcurrentHashMap<>();
    }

    public void register(String eventName, EventListener listener) {
        listeners.computeIfAbsent(eventName, k -> new CopyOnWriteArrayList<>())
                 .add(listener);
    }

    public void unregister(String eventName, EventListener listener) {
        CopyOnWriteArrayList<EventListener> eventListeners = listeners.get(eventName);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    public void emit(String eventName, Object data) {
        CopyOnWriteArrayList<EventListener> eventListeners = listeners.get(eventName);
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                listener.onEvent(eventName, data);
            }
        }
    }

    public void clearAll() {
        listeners.clear();
    }

    public interface EventListener {
        void onEvent(String eventName, Object data);
    }
}
