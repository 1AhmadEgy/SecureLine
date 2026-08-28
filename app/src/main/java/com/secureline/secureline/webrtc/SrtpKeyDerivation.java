package com.secureline.secureline.webrtc;

import com.secureline.secureline.crypto.HKDF;

public class SrtpKeyDerivation {

    private static final int MASTER_KEY_LENGTH = 16;
    private static final int MASTER_SALT_LENGTH = 14;

    public static byte[] deriveSrtpMasterKey(byte[] sharedSecret) {
        return HKDF.deriveKey(sharedSecret, null, 
            "SRTP-MASTER-KEY".getBytes(), MASTER_KEY_LENGTH);
    }

    public static byte[] deriveSrtpMasterSalt(byte[] sharedSecret) {
        return HKDF.deriveKey(sharedSecret, null, 
            "SRTP-MASTER-SALT".getBytes(), MASTER_SALT_LENGTH);
    }

    public static byte[] deriveEncryptionKey(byte[] masterKey) {
        return HKDF.deriveKey(masterKey, null, 
            "SRTP-ENCRYPTION-KEY".getBytes(), 16);
    }

    public static byte[] deriveAuthenticationKey(byte[] masterKey) {
        return HKDF.deriveKey(masterKey, null, 
            "SRTP-AUTH-KEY".getBytes(), 20);
    }
}
