#!/usr/bin/env bash
set -euo pipefail
TASK_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
: "${ANDROID_JAR:?Set ANDROID_JAR}"
: "${JSON_JAR:?Set JSON_JAR to org.json JSON-java (tested 20260814)}"
mkdir -p "$TASK_ROOT/build/crypto-test"
javac -cp "$JSON_JAR:$ANDROID_JAR" -d "$TASK_ROOT/build/crypto-test" "$TASK_ROOT/tools/crypto-test/android/util/Base64.java" "$TASK_ROOT/app/src/main/java/com/sadienotes/app/Crypto.java" "$TASK_ROOT/tools/crypto-test/com/sadienotes/app/CryptoTest.java"
java -cp "$TASK_ROOT/build/crypto-test:$JSON_JAR:$ANDROID_JAR" com.sadienotes.app.CryptoTest
