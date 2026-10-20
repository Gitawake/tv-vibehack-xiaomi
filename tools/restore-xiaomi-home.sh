#!/bin/sh
set -eu
: "${TV_ADDRESS:?Set TV_ADDRESS to your TV host:port}"
adb connect "$TV_ADDRESS"
adb -s "$TV_ADDRESS" shell cmd package set-home-activity --user 0 com.mitv.tvhome/.MainActivityUserMode
adb -s "$TV_ADDRESS" shell input keyevent 3
echo "Check the TV: Xiaomi home should now be the default. No apps were uninstalled."
