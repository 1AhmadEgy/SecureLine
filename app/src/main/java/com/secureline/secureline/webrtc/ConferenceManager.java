package com.secureline.secureline.webrtc;

import java.util.HashMap;
import java.util.Map;

public class ConferenceManager {

    private final Map<String, ConferenceParticipant> participants;

    public ConferenceManager() {
        participants = new HashMap<>();
    }

    public void addParticipant(String participantId, String displayName) {
        ConferenceParticipant participant = new ConferenceParticipant(participantId, displayName);
        participants.put(participantId, participant);
    }

    public void removeParticipant(String participantId) {
        participants.remove(participantId);
    }

    public void muteParticipant(String participantId) {
        ConferenceParticipant participant = participants.get(participantId);
        if (participant != null) {
            participant.setMuted(true);
        }
    }

    public void unmuteParticipant(String participantId) {
        ConferenceParticipant participant = participants.get(participantId);
        if (participant != null) {
            participant.setMuted(false);
        }
    }

    public boolean isParticipantMuted(String participantId) {
        ConferenceParticipant participant = participants.get(participantId);
        return participant != null && participant.isMuted();
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public void endConference() {
        participants.clear();
    }

    private static class ConferenceParticipant {
        private final String id;
        private final String displayName;
        private boolean muted;
        private boolean speaking;

        ConferenceParticipant(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
            this.muted = false;
            this.speaking = false;
        }

        String getId() {
            return id;
        }

        String getDisplayName() {
            return displayName;
        }

        boolean isMuted() {
            return muted;
        }

        void setMuted(boolean muted) {
            this.muted = muted;
        }

        boolean isSpeaking() {
            return speaking;
        }

        void setSpeaking(boolean speaking) {
            this.speaking = speaking;
        }
    }
}
