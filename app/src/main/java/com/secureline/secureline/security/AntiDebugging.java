package com.secureline.secureline.security;

import android.os.Debug;

public class AntiDebugging {

    public static boolean isDebuggerConnected() {
        return Debug.isDebuggerConnected();
    }

    public static boolean isBeingTraced() {
        try {
            java.io.File file = new java.io.File("/proc/self/status");
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(file)
            );
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TracerPid:")) {
                    int tracerPid = Integer.parseInt(line.substring(10).trim());
                    return tracerPid != 0;
                }
            }
            reader.close();
        } catch (Exception e) {
            // Ignore
        }
        return false;
    }

    public static void terminateIfDebugged() {
        if (isDebuggerConnected() || isBeingTraced()) {
            Runtime.getRuntime().exit(0);
        }
    }
}
