#!/bin/bash
echo "Running SecureLine tests..."
cd app
./gradlew test
./gradlew connectedAndroidTest
echo "Tests completed"
