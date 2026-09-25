# SecureLine Security Baseline

## Scope

SecureLine contains Android, Java, and TypeScript components. This document defines the minimum security requirements for changes.

## Secrets

- No passwords, API keys, database credentials, private keys, or production secrets may be committed.
- Runtime configuration must come from environment variables or a managed secret store.
- Development defaults must be non-sensitive placeholders.

## Cryptography

- Do not invent or combine cryptographic protocols without a documented security design and review.
- Application code must distinguish authenticated encryption, key agreement, identity keys, and transport security.
- The server must never receive plaintext message content.

## Authentication

- Authentication must use the selected identity provider consistently.
- Session state must not depend on process memory for production operation.
- Authorization must be checked for every message, contact, and audit resource.

## Logging

- Never log message plaintext, encryption keys, authentication tokens, or complete nonces.
- IP addresses and security telemetry require an explicit retention policy.

## CI

Every pull request must pass Android compilation/tests and backend/web typecheck/build before merge.
