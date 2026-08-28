package com.secureline.secureline.webrtc;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;

public class AudioRoutingManager {

    private final AudioManager audioManager;

    public AudioRoutingManager(Context context) {
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void routeToSpeaker() {
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(true);
        }
    }

    public void routeToEarpiece() {
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(false);
        }
    }

    public void routeToBluetooth() {
        if (audioManager != null) {
            audioManager.setBluetoothScoOn(true);
            audioManager.startBluetoothSco();
        }
    }

    public void stopBluetoothRouting() {
        if (audioManager != null) {
            audioManager.stopBluetoothSco();
            audioManager.setBluetoothScoOn(false);
        }
    }

    public boolean isBluetoothConnected() {
        if (audioManager == null) return false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO ||
                    device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP) {
                    return true;
                }
            }
        }
        return false;
    }
}
