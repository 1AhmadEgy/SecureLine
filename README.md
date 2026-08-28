# SecureLine

أقوى تطبيق اتصالات مشفر في العالم.

## المميزات
- تشفير Signal Protocol (X25519 + AES-256 + ChaCha20)
- طبقة تعمية فريدة (ObfuscationLayer)
- شبكة Tor (اختياري)
- قاعدة بيانات مشفرة (SQLCipher)
- واجهة AMOLED سوداء خفيفة
- خادم مستقل

## البناء
```bash
cd app
./gradlew assembleRelease
```

الخادم

```bash
cd server
docker-compose up -d
```

المساهمة

افتح Issue أو قدّم Pull Request.

الترخيص

GPLv3
