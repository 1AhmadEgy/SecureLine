package com.secureline.secureline.network;

import java.util.ArrayList;
import java.util.List;

public class ServerSyncManager {

    private final List<SyncListener> listeners;
    private boolean isSyncing;

    public ServerSyncManager() {
        listeners = new ArrayList<>();
        isSyncing = false;
    }

    public void startSync() {
        isSyncing = true;
        notifySyncStarted();
    }

    public void endSync() {
        isSyncing = false;
        notifySyncCompleted();
    }

    public boolean isSyncing() {
        return isSyncing;
    }

    public void addListener(SyncListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SyncListener listener) {
        listeners.remove(listener);
    }

    private void notifySyncStarted() {
        for (SyncListener listener : listeners) {
            listener.onSyncStarted();
        }
    }

    private void notifySyncCompleted() {
        for (SyncListener listener : listeners) {
            listener.onSyncCompleted();
        }
    }

    public interface SyncListener {
        void onSyncStarted();
        void onSyncCompleted();
    }
}
