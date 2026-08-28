package com.secureline.secureline.webrtc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioSinkManager {

    private final Queue<AudioFrame> playbackQueue;
    private boolean isPlaying;
    private AudioPlaybackListener listener;

    public AudioSinkManager() {
        playbackQueue = new ConcurrentLinkedQueue<>();
        isPlaying = false;
    }

    public void startPlayback() {
        isPlaying = true;
    }

    public void stopPlayback() {
        isPlaying = false;
        playbackQueue.clear();
    }

    public void addFrameForPlayback(AudioFrame frame) {
        if (isPlaying) {
            playbackQueue.offer(frame);
        }
    }

    public AudioFrame getNextFrameToPlay() {
        return playbackQueue.poll();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setListener(AudioPlaybackListener listener) {
        this.listener = listener;
    }

    public interface AudioPlaybackListener {
        void onFramePlayed(AudioFrame frame);
    }
}
