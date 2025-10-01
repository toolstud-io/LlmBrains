#!/usr/bin/env bash

echo ">>> Build version $(cat VERSION.md)"
./gradlew -q build &&  ./gradlew -q buildPlugin
ls -rtl build/distributions