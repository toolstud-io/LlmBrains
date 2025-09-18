# LLM Brains

"LLM Brains" is a JetBrains IDE plugin (for e.g. usage in PhpStorm) to open any (popular) CLI coding agent in a new terminal window.
It's like the Claude Code button but also provides OpenAI Codex and Google's Gemini CLI, and maybe more in the future.

It shows this icon in the top right corner of the IDE: 🫴. Mouseover text: "Open any CLI coding agent in a new terminal window."

when you click on that icon, gives you the following options:

* "Claude (Anthropic)" ⇒ run 'claude' in an IDE terminal window with title '🫴 Claude'
* "Codex (OpenAI)" ⇒ run 'codex' in an IDE terminal window with title '🫴 Codex'
* "Gemini (Google)" ⇒ run 'gemini' in an IDE terminal window with title '🫴 Gemini'
* "Check what's installed" ⇒ run a bash script in an IDE terminal window: for each of the CLI programs above: if it's installed, show the version. If it's not, show how to install it.


## Docker-backed Gradle

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


## Development remarks

This plugin was developed in Kotlin with OpenAI Codex CLI. 

![Developed in Kotlin with OpenAI Codex CLI](https://img.shields.io/badge/Developed_with-🫴_Codex_CLI-orange)