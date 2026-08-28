package com.secureline.secureline.security;

import java.util.Timer;
import java.util.TimerTask;

public class SelfDestruct {

    private Timer timer;

    public void scheduleDestruct(long delayMillis, Runnable destructAction) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                destructAction.run();
            }
        }, delayMillis);
    }

    public void cancelDestruct() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
