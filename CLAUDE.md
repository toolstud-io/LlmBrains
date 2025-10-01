# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

LLM Brains is a JetBrains IDE plugin that adds a toolbar button (🫴) to launch popular CLI coding agents (Claude, Codex, Gemini, Qodo) in IDE terminal windows. It targets platform family 251-252.* and requires IntelliJ Platform 2023.1+ with the built-in Terminal plugin.

## Build & Development Commands

Build and test:
```bash
./gradlew build           # Compile, verify, and create plugin ZIP
./gradlew test            # Run unit and integration tests
./gradlew runIde          # Launch sandbox IDE with plugin installed
```

Code formatting:
```bash
./gradlew ktlintFormat    # Auto-format Kotlin code before committing
```

Documentation preview:
```bash
mkdocs serve              # Preview documentation at http://localhost:8000
```

## Architecture

### Core Components

- **LlmBrainsActionGroup** (`src/main/kotlin/com/forret/llmbrains/LlmBrainsActionGroup.kt`): Main action group that creates the toolbar dropdown menu, dynamically populates actions based on enabled agents, and generates bash scripts for checking/updating agents
- **CodingAgents** (`src/main/kotlin/com/forret/llmbrains/CodingAgents.kt`): Data model and registry for all supported coding agents (Claude, Codex, Gemini, Qodo) with their command, version args, install/update hints
- **AgentSettingsState** (`src/main/kotlin/com/forret/llmbrains/AgentSettingsState.kt`): Persistent settings storage that tracks which agents are enabled/disabled
- **AgentSettingsConfigurable** (`src/main/kotlin/com/forret/llmbrains/AgentSettingsConfigurable.kt`): Settings UI panel accessible via Preferences > Tools > LLM Brains
- **TerminalCommandRunner** (`src/main/kotlin/com/forret/llmbrains/TerminalCommandRunner.kt`): Executes commands in new IDE terminal windows using `TerminalToolWindowManager`
- **LlmBrainsScriptInstaller** (`src/main/kotlin/com/forret/llmbrains/LlmBrainsScriptInstaller.kt`): Manages installation of both bash and PowerShell helper scripts to the IDE config directory
- **OsDetector** (`src/main/kotlin/com/forret/llmbrains/OsDetector.kt`): Detects operating system (Windows/Mac/Linux) to select the appropriate script

### Plugin Registration

`src/main/resources/META-INF/plugin.xml` defines:
- Plugin metadata (id, name, vendor, dependencies)
- The main toolbar action group added to `MainToolbarRight`
- Application-level settings configurable

### Helper Scripts

The plugin includes platform-specific helper scripts:
- **llmbrains.sh** (Unix/Linux/Mac): Bash script for Unix-like systems
- **llmbrains.ps1** (Windows): PowerShell script for Windows

Both scripts support four subcommands:
- `check <name> <version-command> [install-hint]`: Check if an agent is installed
- `update <name> <binary> <update-command> [install-hint]`: Update an installed agent
- `check-all <agent-definitions>`: Check all agents (accepts pipe-separated data)
- `update-all <agent-definitions> <active-agent-ids>`: Update all enabled agents

Scripts are installed to the IDE config directory and called by the plugin to avoid complex shell escaping in Kotlin.

## Key Implementation Details

### Version Management
The plugin version is read from `VERSION.md` at build time (see `build.gradle.kts:7`).

### Platform Compatibility
- `sinceBuild.set("251")` and `untilBuild.set("252.*")` in `build.gradle.kts` define compatible IDE versions
- Targets IntelliJ Platform 2023.1 to maximize compatibility across JetBrains IDEs

### Cross-Platform Support & Shell Escaping
- `OsDetector.isWindows()` determines whether to use PowerShell or bash
- `escapeForDoubleQuotes()`: Escapes bash special characters ($, ", \, `)
- `escapeForPowerShell()`: Escapes PowerShell single quotes (using double single-quotes)
- `buildWindowsCheckScript()` and `buildWindowsUpdateScript()` generate PowerShell commands
- Unix systems use `bash -lc` with PATH manipulation; Windows uses `powershell -NoProfile -Command`

### Dynamic Action Generation
The dropdown menu is built dynamically in `LlmBrainsActionGroup.getChildren()` based on which agents are enabled in settings, ensuring the UI always reflects current configuration.

## Coding Conventions

- Kotlin with four-space indentation, trailing commas enabled
- PascalCase for classes/actions, camelCase for methods/properties, SCREAMING_SNAKE_CASE for constants
- Immutable `val` by default
- Prefix actions that are part of the LLM Brains feature with `LlmBrains` prefix
- Use `DumbAware` marker interface for actions that work during indexing

## Testing

- Place tests in `src/test/kotlin` mirroring main package structure
- Use JUnit 5 and MockK for mocking
- Name test classes `<Subject>Test` with methods using descriptive `should...` phrases
- Cover both success and failure cases for actions wrapping shell commands
- Generate coverage with `./gradlew jacocoTestReport` (target: >80%)

## Commit Conventions

Use Conventional Commit prefixes: `feat`, `fix`, `docs`, `chore`, etc.
Reference issues with `(#123)` when applicable.