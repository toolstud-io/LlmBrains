#!/usr/bin/env bash
set -uo pipefail

usage() {
  cat <<'USAGE'
Usage:
  llmbrains.sh check <friendly-name> <version-command> [install-hint]
  llmbrains.sh update <friendly-name> <binary> <update-command> [install-hint]
USAGE
}

if [[ $# -lt 1 ]]; then
  usage
  exit 1
fi

subcommand="$1"
shift

case "$subcommand" in
  check)
    if [[ $# -lt 2 ]]; then
      echo "llmbrains.sh: check requires a name and version command" >&2
      usage >&2
      exit 1
    fi
    name="$1"
    version_command="$2"
    install_hint="${3:-}"

    binary="${version_command%% *}"
    if [[ -z "$binary" ]]; then
      echo "llmbrains.sh: could not determine binary for $name" >&2
      exit 1
    fi

    if command -v "$binary" >/dev/null 2>&1; then
      version_output=$(eval "$version_command" 2>&1)
      status=$?
      if [[ $status -eq 0 ]]; then
        echo "- $name is installed: $version_output"
      else
        echo "- $name is installed but the version command failed (exit $status): $version_output"
      fi
    else
      if [[ -n "$install_hint" ]]; then
        echo "( $name is NOT installed. You can install it with: $install_hint )"
      else
        echo "( $name is NOT installed. )"
      fi
    fi
    ;;
  update)
    if [[ $# -lt 3 ]]; then
      echo "llmbrains.sh: update requires a name, binary, and update command" >&2
      usage >&2
      exit 1
    fi
    name="$1"
    binary="$2"
    update_command="$3"
    install_hint="${4:-}"

    if command -v "$binary" >/dev/null 2>&1; then
      echo "- Updating $name..."
      update_output=$(eval "$update_command" 2>&1)
      status=$?
      if [[ $status -eq 0 ]]; then
        if [[ -n "$update_output" ]]; then
          echo "$update_output"
        fi
        echo "  Update completed."
      else
        echo "$update_output"
        echo "! Failed to update $name (exit $status)."
      fi
    else
      hint="$install_hint"
      if [[ -z "$hint" ]]; then
        hint="install instructions unavailable"
      fi
      echo "! $name is not installed. Run: $hint"
    fi
    echo
    ;;
  *)
    echo "llmbrains.sh: unknown subcommand '$subcommand'" >&2
    usage >&2
    exit 1
    ;;
esac
