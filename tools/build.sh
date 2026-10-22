#!/usr/bin/env bash
set -euo pipefail
TASK_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
TASK_SDK="${SADIE_BUILD_SDK:?Set SADIE_BUILD_SDK to the Android build tools/JDK directory}"
TASK_JDK="$(find "$TASK_SDK" -maxdepth 1 -type d -name 'jdk-*' | head -1)"
export JAVA_HOME="$TASK_JDK"
export PATH="$TASK_JDK/bin:$PATH"
TASK_BT="$TASK_SDK/android-15"
TASK_PLATFORM="$TASK_SDK/android-35/android.jar"
TASK_BUILD="$TASK_ROOT/build"
TASK_KEY="${SADIE_SIGNING_KEY:?Set SADIE_SIGNING_KEY to an existing keystore}"
: "${SADIE_KEY_PASSWORD:?Set SADIE_KEY_PASSWORD}"
mkdir -p "$TASK_BUILD/classes" "$TASK_BUILD/dex" "$TASK_BUILD/generated" "$TASK_ROOT/outputs"
"$TASK_BT/aapt2" compile --dir "$TASK_ROOT/app/src/main/res" -o "$TASK_BUILD/resources.zip"
"$TASK_BT/aapt2" link -I "$TASK_PLATFORM" --manifest "$TASK_ROOT/app/src/main/AndroidManifest.xml" --java "$TASK_BUILD/generated" -A "$TASK_ROOT/app/src/main/assets" "$TASK_BUILD/resources.zip" -o "$TASK_BUILD/unsigned.apk"
javac -source 8 -target 8 -classpath "$TASK_PLATFORM" -d "$TASK_BUILD/classes" "$TASK_ROOT/app/src/main/java/com/sadienotes/app/"*.java "$TASK_BUILD/generated/com/sadienotes/app/R.java"
mapfile -t TASK_CLASSES < <(find "$TASK_BUILD/classes" -name '*.class')
"$TASK_BT/d8" --lib "$TASK_PLATFORM" --min-api 26 --output "$TASK_BUILD/dex" "${TASK_CLASSES[@]}"
python3 - "$TASK_BUILD" <<'PY'
import sys,zipfile,pathlib
b=pathlib.Path(sys.argv[1])
with zipfile.ZipFile(b/'unsigned.apk','a',zipfile.ZIP_DEFLATED) as z:z.write(b/'dex/classes.dex','classes.dex')
PY
"$TASK_BT/zipalign" -f -p 4 "$TASK_BUILD/unsigned.apk" "$TASK_BUILD/aligned.apk"
"$TASK_BT/apksigner" sign --ks "$TASK_KEY" --ks-pass env:SADIE_KEY_PASSWORD --key-pass env:SADIE_KEY_PASSWORD --out "$TASK_ROOT/outputs/SadieNotes.apk" "$TASK_BUILD/aligned.apk"
"$TASK_BT/apksigner" verify --verbose "$TASK_ROOT/outputs/SadieNotes.apk"
