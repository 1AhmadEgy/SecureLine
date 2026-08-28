# SecureLine Threat Model

## Adversaries

### 1. Passive Network Observer
- Can see: message sizes, timing, IP addresses
- Cannot see: message content, identities
- Mitigation: Encryption, padding, Tor

### 2. Active Network Attacker
- Can: modify, drop, replay packets
- Cannot: decrypt messages
- Mitigation: HMAC, sequence numbers, TLS

### 3. Malicious Server Operator
- Can: see metadata, delete messages
- Cannot: read message content
- Mitigation: End-to-end encryption, metadata protection

### 4. Government Agency
- Can: compel server operator, monitor network
- Cannot: decrypt without keys
- Mitigation: Forward secrecy, Tor, no phone number

### 5. Device Theft
- Can: access device storage
- Cannot: decrypt without biometric auth
- Mitigation: SQLCipher, Keystore, remote wipe

## Attack Surfaces

| Surface | Risk | Mitigation |
|---------|------|------------|
| Key Exchange | High | X25519 with verification |
| Message Storage | Medium | SQLCipher AES-256 |
| Network Transport | Medium | TLS 1.3 + Tor |
| Memory | Low | In-memory encryption |
| UI | Low | Anti-tamper |
| Server | Medium | Minimal data retention |
