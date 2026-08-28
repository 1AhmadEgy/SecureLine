package com.secureline.secureline.security;

import java.util.ArrayList;
import java.util.List;

public class SecurityEventLogger {

    private final List<SecurityEvent> events;
    private static final int MAX_EVENTS = 1000;

    public SecurityEventLogger() {
        events = new ArrayList<>();
    }

    public void logEvent(String eventType, String description) {
        SecurityEvent event = new SecurityEvent(eventType, description);
        events.add(event);
        if (events.size() > MAX_EVENTS) {
            events.remove(0);
        }
    }

    public List<SecurityEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public void clearEvents() {
        events.clear();
    }

    public static class SecurityEvent {
        private final String eventType;
        private final String description;
        private final long timestamp;

        public SecurityEvent(String eventType, String description) {
            this.eventType = eventType;
            this.description = description;
            this.timestamp = System.currentTimeMillis();
        }

        public String getEventType() {
            return eventType;
        }

        public String getDescription() {
            return description;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
