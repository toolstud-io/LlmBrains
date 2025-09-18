#!/bin/bash

# check_version.sh - Check if a CLI tool is installed and show version
# Usage: check_version.sh "Tool Name" "command" "version_flag" "install_command"

name="$1"
cmd="$2"
ver_flag="$3"
install="$4"

if command -v "$cmd" >/dev/null 2>&1; then
  if [ -n "$ver_flag" ]; then
    version_output=$($cmd $ver_flag 2>/dev/null | head -n 1)
    echo "✅  $name installed: $version_output"
  else
    echo "✅  $name installed: $cmd"
  fi
else
  echo "❌  $name not found. Install: $install"
fi