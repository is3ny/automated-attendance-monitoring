#!/bin/bash

# git fetch --unshallow #required for commit count

./gradlew clean assembleStandardDebug

COMMIT_COUNT=$(git rev-list --count HEAD)
export ARTIFACT="aam-r${COMMIT_COUNT}.apk"

mv app/build/outputs/apk/debug/app-debug.apk $ARTIFACT
