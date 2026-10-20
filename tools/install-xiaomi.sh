#!/bin/sh
# Install only; keep stock launcher and all system services enabled.
set -eu
: "${TV_ADDRESS:?Set TV_ADDRESS to your TV host:port}"
ROOT=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
APK=${APK_PATH:-"$ROOT/launcher/app/build/outputs/apk/release/app-release.apk"}
[ -f "$APK" ] || { echo "Build the release APK first." >&2; exit 1; }
adb connect "$TV_ADDRESS"
adb -s "$TV_ADDRESS" install -r "$APK"
adb -s "$TV_ADDRESS" shell am start -n com.gathin.tvhome/.MainActivity
adb -s "$TV_ADDRESS" shell cmd package set-home-activity --user 0 com.gathin.tvhome/.MainActivity
adb -s "$TV_ADDRESS" shell input keyevent 3
echo "Check the TV home screen. Stock Xiaomi home remains installed."
