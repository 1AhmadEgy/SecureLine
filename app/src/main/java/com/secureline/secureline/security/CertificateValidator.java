package com.secureline.secureline.security;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.X509TrustManager;

public class CertificateValidator implements X509TrustManager {

    private final String pinnedCertificateHash;

    public CertificateValidator(String pinnedHash) {
        this.pinnedCertificateHash = pinnedHash;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
        // Accept all client certificates
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        if (chain == null || chain.length == 0) {
            throw new IllegalArgumentException("No certificates provided");
        }

        X509Certificate serverCertificate = chain[0];

        checkValidity(serverCertificate);
        checkPinning(serverCertificate);
    }

    private void checkValidity(X509Certificate certificate) {
        certificate.checkValidity(new Date());
    }

    private void checkPinning(X509Certificate certificate) {
        String certificateHash = HashUtils.sha256Hex(certificate.getEncoded());
        if (!pinnedCertificateHash.equals(certificateHash)) {
            throw new java.security.cert.CertificateException(
                "Certificate pinning failed"
            );
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
