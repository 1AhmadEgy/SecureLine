package com.secureline.secureline.webrtc;

import java.util.HashMap;
import java.util.Map;

public class CallSecurityManager {

    private final Map<String, CallSecurityInfo> callSecurityInfos;

    public CallSecurityManager() {
        callSecurityInfos = new HashMap<>();
    }

    public void registerCall(String callId, byte[] sessionKey) {
        CallSecurityInfo info = new CallSecurityInfo(sessionKey);
        callSecurityInfos.put(callId, info);
    }

    public boolean isCallSecure(String callId) {
        CallSecurityInfo info = callSecurityInfos.get(callId);
        return info != null && info.isSecure();
    }

    public void markCallInsecure(String callId) {
        CallSecurityInfo info = callSecurityInfos.get(callId);
        if (info != null) {
            info.markInsecure();
        }
    }

    public void endCall(String callId) {
        callSecurityInfos.remove(callId);
    }

    private static class CallSecurityInfo {
        private final byte[] sessionKey;
        private boolean secure;

        CallSecurityInfo(byte[] key) {
            this.sessionKey = key;
            this.secure = true;
        }

        boolean isSecure() {
            return secure;
        }

        void markInsecure() {
            secure = false;
        }
    }
}
