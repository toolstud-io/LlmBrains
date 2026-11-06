# Gotchas

One line each, newest last. Start a line with `path/to/file:` and the fodo commands show it when that file is in your diff.

- 2026-11-06 build/distributions/: building without bumping VERSION.md silently overwrites the released ZIP of that version; run setver before building a release
- 2026-11-06 CLAUDE.md: 'Targets IntelliJ Platform 2023.1' is stale (the build compiles against 2025.1) and the ktlintFormat task it lists does not exist
- 2026-11-06 src/main/kotlin/com/forret/llmbrains/TerminalCommandRunner.kt: the Plugin Verifier does not cover its reflective terminal calls, so 'Compatible' says nothing about them; test them in the real IDE
