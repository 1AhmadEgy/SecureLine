package com.secureline.secureline.webrtc;

import android.content.Context;
import android.media.AudioManager;

public class AudioDeviceManager {

    private final AudioManager audioManager;
    private boolean speakerEnabled;
    private boolean microphoneMuted;

    public AudioDeviceManager(Context context) {
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.speakerEnabled = false;
        this.microphoneMuted = false;
    }

    public void enableSpeaker() {
        speakerEnabled = true;
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(true);
        }
    }

    public void disableSpeaker() {
        speakerEnabled = false;
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(false);
        }
    }

    public void toggleSpeaker() {
        if (speakerEnabled) {
            disableSpeaker();
        } else {
            enableSpeaker();
        }
    }

    public void muteMicrophone() {
        microphoneMuted = true;
        if (audioManager != null) {
            audioManager.setMicrophoneMute(true);
        }
    }

    public void unmuteMicrophone() {
        microphoneMuted = false;
        if (audioManager != null) {
            audioManager.setMicrophoneMute(false);
        }
    }

    public void toggleMicrophone() {
        if (microphoneMuted) {
            unmuteMicrophone();
        } else {
            muteMicrophone();
        }
    }

    public boolean isSpeakerEnabled() {
        return speakerEnabled;
    }

    public boolean isMicrophoneMuted() {
        return microphoneMuted;
    }

    public void setAudioModeForCall() {
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        }
    }

    public void setAudioModeNormal() {
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
        }
    }
}
