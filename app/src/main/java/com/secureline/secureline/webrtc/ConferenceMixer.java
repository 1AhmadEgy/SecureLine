package com.secureline.secureline.webrtc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConferenceMixer {

    private final List<short[]> participantAudioBuffers;

    public ConferenceMixer() {
        participantAudioBuffers = new CopyOnWriteArrayList<>();
    }

    public void addParticipantAudio(short[] audio) {
        participantAudioBuffers.add(audio);
    }

    public short[] mixAllParticipants() {
        if (participantAudioBuffers.isEmpty()) return new short[0];

        int maxLength = 0;
        for (short[] audio : participantAudioBuffers) {
            maxLength = Math.max(maxLength, audio.length);
        }

        short[] mixed = new short[maxLength];
        for (short[] audio : participantAudioBuffers) {
            for (int i = 0; i < audio.length; i++) {
                int sum = mixed[i] + audio[i];
                if (sum > Short.MAX_VALUE) sum = Short.MAX_VALUE;
                if (sum < Short.MIN_VALUE) sum = Short.MIN_VALUE;
                mixed[i] = (short) sum;
            }
        }
        return mixed;
    }

    public void clearBuffers() {
        participantAudioBuffers.clear();
    }

    public int getParticipantCount() {
        return participantAudioBuffers.size();
    }
}
