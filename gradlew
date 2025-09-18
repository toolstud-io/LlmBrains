#!/usr/bin/env bash
set -euo pipefail

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is required to run gradle tasks." >&2
  exit 1
fi

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
PROJECT_ROOT=$SCRIPT_DIR
IMAGE=${GRADLE_DOCKER_IMAGE:-gradle:8.7-jdk17-jammy}
CACHE_ROOT=${GRADLE_DOCKER_CACHE:-$HOME/.cache/mkdox-gradle}
GRADLE_CACHE_DIR="$CACHE_ROOT/gradle"
HOME_CACHE_DIR="$CACHE_ROOT/home"

mkdir -p "$GRADLE_CACHE_DIR" "$HOME_CACHE_DIR"

USER_FLAG=(--user "$(id -u):$(id -g)")
if [[ "${GRADLE_DOCKER_PRESERVE_OWNERSHIP:-1}" == "0" ]]; then
  USER_FLAG=()
fi

EXTRA_FLAGS=()
if [[ -n "${GRADLE_DOCKER_FLAGS:-}" ]]; then
  # shellcheck disable=SC2206
  EXTRA_FLAGS=( ${GRADLE_DOCKER_FLAGS} )
fi

CMD=(docker run --rm)

if [[ ${#USER_FLAG[@]} -gt 0 ]]; then
  CMD+=("${USER_FLAG[@]}")
fi

CMD+=(-e "GRADLE_USER_HOME=/gradle-cache")
CMD+=(-e "HOME=/home/runner")

if [[ -n "${GRADLE_OPTS:-}" ]]; then
  CMD+=(-e "GRADLE_OPTS=${GRADLE_OPTS}")
fi

if [[ -n "${JAVA_TOOL_OPTIONS:-}" ]]; then
  CMD+=(-e "JAVA_TOOL_OPTIONS=${JAVA_TOOL_OPTIONS}")
fi

CMD+=(-v "$GRADLE_CACHE_DIR":/gradle-cache)
CMD+=(-v "$HOME_CACHE_DIR":/home/runner)
CMD+=(-v "$PROJECT_ROOT":/workspace)
CMD+=(-w /workspace)

if [[ ${#EXTRA_FLAGS[@]} -gt 0 ]]; then
  CMD+=("${EXTRA_FLAGS[@]}")
fi

CMD+=(--entrypoint gradle)
CMD+=("$IMAGE")

if [[ $# -gt 0 ]]; then
  CMD+=("$@")
fi

exec "${CMD[@]}"
