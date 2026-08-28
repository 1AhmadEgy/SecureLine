package com.secureline.secureline.security;

import java.util.ArrayList;
import java.util.List;

public class SecureLogger {

    private static final int MAX_ENTRIES = 500;
    private final List<LogEntry> entries;
    private boolean loggingEnabled;

    public SecureLogger() {
        entries = new ArrayList<>();
        loggingEnabled = true;
    }

    public void log(String category, String message) {
        if (!loggingEnabled) return;
        entries.add(new LogEntry(category, message));
        if (entries.size() > MAX_ENTRIES) {
            entries.remove(0);
        }
    }

    public List<LogEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void clear() {
        entries.clear();
    }

    public void setLoggingEnabled(boolean enabled) {
        this.loggingEnabled = enabled;
    }

    public static class LogEntry {
        private final String category;
        private final String message;
        private final long timestamp;

        public LogEntry(String category, String message) {
            this.category = category;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getCategory() {
            return category;
        }

        public String getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
