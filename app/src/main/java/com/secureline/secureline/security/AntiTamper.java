package com.secureline.secureline.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;

public class AntiTamper {

    public static String getAppSignature(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(
                context.getPackageName(),
                PackageManager.GET_SIGNATURES
            );

            Signature signature = info.signatures[0];
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(signature.toByteArray());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verifyIntegrity(Context context, String expectedSignature) {
        String actualSignature = getAppSignature(context);
        return actualSignature != null && actualSignature.equals(expectedSignature);
    }

    public static boolean isDebugBuild() {
        return (android.os.Debug.isDebuggerConnected() || 
                android.os.Debug.waitingForDebugger());
    }
}
