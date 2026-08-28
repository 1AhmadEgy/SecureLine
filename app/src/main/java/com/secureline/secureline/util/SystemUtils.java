package com.secureline.secureline.util;

import android.os.Environment;
import android.os.StatFs;

public class SystemUtils {

    public static long getAvailableStorageSpace() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return statFs.getAvailableBytes();
        } catch (Exception e) {
            return -1;
        }
    }

    public static long getTotalStorageSpace() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return statFs.getTotalBytes();
        } catch (Exception e) {
            return -1;
        }
    }

    public static long getAvailableMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.freeMemory();
    }

    public static long getTotalMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory();
    }

    public static long getMaxMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.maxMemory();
    }

    public static void gc() {
        System.gc();
    }
}
