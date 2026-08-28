# Security Policy

## Encryption Protocols
- X25519 (key exchange)
- AES-256-GCM (voice calls)
- ChaCha20-Poly1305 (text messages)
- HMAC-SHA256 (integrity)

## Additional Layers
- Custom ObfuscationLayer
- Tor Network (optional)
- SQLCipher (local database)
- Android Keystore (key storage)

## Reporting Vulnerabilities
Submit to: security@secureline.example.com

## Known Limitations
- Metadata visible to server admin (unless Tor enabled)
- Tor mode increases latency
