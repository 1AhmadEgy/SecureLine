# SecureLine Architecture

## Overview
SecureLine is an encrypted communication application built on Signal Protocol with additional security layers.

## Components

### Client (Android)
- Signal-Android base
- ObfuscationLayer (custom data obfuscation)
- TorManager (optional Tor routing)
- KeyManager (hardware-backed key storage)
- CallQualityManager (Opus codec tuning)
- SQLCipher (encrypted local database)

### Server
- Signal-Server
- PostgreSQL (main database)
- Redis (cache)
- Docker Compose (deployment)

### Security Layers
1. Signal Protocol (X25519 + AES-256-GCM + ChaCha20-Poly1305)
2. ObfuscationLayer (byte reversal + dummy padding)
3. Tor Network (optional IP hiding)
4. SQLCipher (database encryption at rest)
5. Android Keystore (hardware key protection)

## Data Flow
1. User A sends message
2. Message encrypted by Signal Protocol
3. Data passed through ObfuscationLayer
4. Sent to server (server cannot decrypt)
5. Server routes to User B
6. User B deobfuscates then decrypts
