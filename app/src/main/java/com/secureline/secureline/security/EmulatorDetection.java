package com.secureline.secureline.security;

import android.os.Build;

public class EmulatorDetection {

    public static boolean isEmulator() {
        return checkBuildFingerprint() || 
               checkHardware() || 
               checkModel() || 
               checkProduct();
    }

    private static boolean checkBuildFingerprint() {
        String fingerprint = Build.FINGERPRINT.toLowerCase();
        return fingerprint.contains("generic") ||
               fingerprint.contains("emulator") ||
               fingerprint.contains("sdk");
    }

    private static boolean checkHardware() {
        String hardware = Build.HARDWARE.toLowerCase();
        return hardware.contains("goldfish") ||
               hardware.contains("ranchu") ||
               hardware.contains("emulator");
    }

    private static boolean checkModel() {
        String model = Build.MODEL.toLowerCase();
        return model.contains("emulator") ||
               model.contains("android sdk") ||
               model.contains("google sdk");
    }

    private static boolean checkProduct() {
        String product = Build.PRODUCT.toLowerCase();
        return product.contains("sdk") ||
               product.contains("emulator") ||
               product.contains("simulator");
    }
}
