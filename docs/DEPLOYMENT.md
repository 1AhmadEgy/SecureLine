# SecureLine Deployment Guide

## Prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- 2GB RAM minimum
- 10GB disk space

## Server Deployment

1. Clone the repository:
```bash
git clone https://github.com/1AhmadEgy/SecureLine.git
cd SecureLine
```

2. Configure environment:

```bash
cd server
nano config.yml
```

3. Start services:

```bash
docker-compose up -d
```

4. Verify health:

```bash
curl http://localhost:8080/health
```

Android App Build

1. Open Android Studio
2. Import app/ directory
3. Add google-services.json to app/
4. Build APK:

```bash
cd app
./gradlew assembleRelease
```

Production Notes

· Use HTTPS (Let's Encrypt)
· Set up firewall rules
· Enable monitoring
· Configure automatic backups
