package com.secureline.secureline.webrtc;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SrtpSessionManager {

    private final Map<String, SrtpSession> sessions;

    public SrtpSessionManager() {
        sessions = new HashMap<>();
    }

    public String createSession() {
        String sessionId = generateSessionId();
        SrtpSession session = new SrtpSession();
        sessions.put(sessionId, session);
        return sessionId;
    }

    public SrtpSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void destroySession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void destroyAllSessions() {
        sessions.clear();
    }

    private String generateSessionId() {
        byte[] random = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(random);
        StringBuilder sb = new StringBuilder();
        for (byte b : random) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static class SrtpSession {
        private byte[] masterKey;
        private byte[] masterSalt;
        private long packetCount;
        private boolean active;

        public SrtpSession() {
            masterKey = new byte[16];
            masterSalt = new byte[14];
            SecureRandom random = new SecureRandom();
            random.nextBytes(masterKey);
            random.nextBytes(masterSalt);
            packetCount = 0;
            active = true;
        }

        public byte[] getMasterKey() {
            return masterKey;
        }

        public byte[] getMasterSalt() {
            return masterSalt;
        }

        public long incrementPacketCount() {
            return ++packetCount;
        }

        public boolean isActive() {
            return active;
        }

        public void deactivate() {
            active = false;
        }
    }
}
