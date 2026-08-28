package com.secureline.secureline.security;

import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

public class CertificatePinner {

    private final Map<String, String> pinnedCertificates;

    public CertificatePinner() {
        pinnedCertificates = new HashMap<>();
    }

    public void pinCertificate(String hostname, String sha256Hash) {
        pinnedCertificates.put(hostname, sha256Hash);
    }

    public boolean verify(String hostname, Certificate certificate) {
        String expectedHash = pinnedCertificates.get(hostname);
        if (expectedHash == null) return true;

        try {
            byte[] certBytes = certificate.getEncoded();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(certBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().equals(expectedHash);
        } catch (Exception e) {
            return false;
        }
    }
}
