#!/usr/bin/env bash

echo ">>> Build version $(cat VERSION.md)"
./gradlew build &&  ./gradlew buildPlugin
ls -rtl build/distributions