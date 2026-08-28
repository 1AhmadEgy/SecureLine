package com.secureline.secureline.webrtc;

import org.webrtc.DataChannel;

import java.nio.ByteBuffer;

public class DataChannelManager {

    private DataChannel dataChannel;

    public void createDataChannel(DataChannel.Init init) {
        // DataChannel creation handled by PeerConnection
    }

    public void setDataChannel(DataChannel channel) {
        this.dataChannel = channel;
        registerObserver();
    }

    private void registerObserver() {
        if (dataChannel == null) return;

        dataChannel.registerObserver(new DataChannel.Observer() {
            @Override
            public void onBufferedAmountChange(long previousAmount) {}

            @Override
            public void onStateChange() {
                if (dataChannel.state() == DataChannel.State.CLOSED) {
                    dataChannel = null;
                }
            }

            @Override
            public void onMessage(DataChannel.Buffer buffer) {
                ByteBuffer data = buffer.data;
                byte[] bytes = new byte[data.remaining()];
                data.get(bytes);
            }
        });
    }

    public void sendData(byte[] data) {
        if (dataChannel != null && dataChannel.state() == DataChannel.State.OPEN) {
            DataChannel.Buffer buffer = new DataChannel.Buffer(
                ByteBuffer.wrap(data), false
            );
            dataChannel.send(buffer);
        }
    }

    public void close() {
        if (dataChannel != null) {
            dataChannel.close();
            dataChannel.dispose();
            dataChannel = null;
        }
    }
}
