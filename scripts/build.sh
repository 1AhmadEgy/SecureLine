#!/bin/bash
cd app
./gradlew assembleRelease
echo "APK generated at: app/build/outputs/apk/release/app-release.apk"
