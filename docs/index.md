# JetBrains mkdox button

JetBrains plugin (for e.g. usage in PhpStorm) where there's a specific [pforret/mkdox](https://github.com/pforret/mkdox) functionality button in the top right corner of the editor, right next to the Claude Code button.

The icon should be ![like this](icon/mkdox-button.png) (or similar).

* there always is the option 'Check mkdox'. This show a list of checkboxes:
    - is there a /docs in the repo Y/N
    - is there a /mkdocs.yml file in the root Y/N
    - is there a /docs/blog folder in the repo Y/N
    - is Docker running Y/N
* if there is no `/docs` folder OR no `/mkdocs.yml` file
    - the icon is inactive (light gray)
    - when the mouse moves over the icon, you can select 'Create mkdox' 
    - if Docker is running, 
        - this executes `mkdox -E "$(basename $root_folder)" new .` and 
        - opens `/.mkdocs.yml` file in editor
    - otherwise, it just warns that Docker should be running
* if there is a `/docs` folder in the project AND there is a `/mkdocs.yml` file in the root AND there is a `/docs/blog` directory
    - then the icon become active (bright light blue color)
    - when the mouse pointer moves over it, there is an option 'Serve mkdox' => this tries `mkdox.sh serve` when available and otherwise executes `mkdox serve` in the root folder
    - if Docker is running,
        - this executes `mkdox serve` in a new terminal window
    - otherwise, it just warns that Docker should be running

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
