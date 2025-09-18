# Development with Docker

This repository includes a Docker-aware Gradle wrapper script (`./gradlew`) so you can execute plugin tasks without installing Java or Gradle locally. The script launches the `gradle:8.7-jdk17-jammy` image, mounts the repository into `/workspace`, and persists Gradle caches under `~/.cache/mkdox-gradle`.

Common commands:

```bash
./gradlew build
./gradlew test
./gradlew runIde
./gradlew buildPlugin
```

Environment knobs:

- `GRADLE_DOCKER_IMAGE` overrides the container image tag.
- `GRADLE_DOCKER_CACHE` changes the host cache location.
- `GRADLE_DOCKER_FLAGS` appends extra `docker run` flags (for example `"--network host"`).
- Set `GRADLE_DOCKER_PRESERVE_OWNERSHIP=0` if you prefer the container defaults for file ownership.

Docker must be running locally for the wrapper to work.

