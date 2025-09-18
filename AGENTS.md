  # Repository Guidelines

  ## Project Structure & Module Organization
  This plugin repository targets JetBrains IDEs and follows the Gradle IntelliJ plugin layout. Place production Kotlin sources in `src/main/kotlin`, organised by feature packages (for example,
  `com.mkdox.actions`). Shared assets such as icons live under `src/main/resources/icons`, while plugin metadata is in `src/main/resources/META-INF/plugin.xml`. Keep samples or docs in `docs/` and
  automation scripts in the repo root. When adding long-running helpers, prefer wrapping them in `mkdox.sh` so the IDE action can call them consistently.

  ## Build, Test, and Development Commands
  Use the Gradle wrapper once added to the project root. `./gradlew build` compiles the plugin and runs verification tasks. `./gradlew runIde` launches a sandbox IDE with the mkdox button installed;
  use it to validate UI changes quickly. `./gradlew test` executes all unit and integration tests. For documentation checks, run `mkdox serve` from the project root to spin up the mkdocs preview used
  by the button.

  ## Coding Style & Naming Conventions
  Write Kotlin with four-space indentation, trailing commas enabled, and immutable vals by default. Follow JetBrains' Kotlin style guide: PascalCase for classes/actions, camelCase for methods and
  properties, and SCREAMING_SNAKE_CASE for constants. Prefix actions that trigger mkdox workflows with `MkDox` (for example, `MkDoxServeAction`) so they are easy to locate in `plugin.xml`. Run `./
  gradlew ktlintFormat` before committing to auto-format code.

  ## Testing Guidelines
  Add unit tests under `src/test/kotlin` mirroring the main package layout, using JUnit 5 and MockK for mocks. Name test classes `<Subject>Test` and methods with descriptive `should...` phrases. When
  a new action wraps a shell command, cover both success and failure branches. Generate coverage reports with `./gradlew jacocoTestReport` and keep overall coverage above 80%.

  ## Commit & Pull Request Guidelines
  Use Conventional Commit prefixes (`feat`, `fix`, `docs`, `chore`, etc.) followed by a short imperative summary. Reference GitHub issues using `(#123)` at the end when applicable. Pull requests
  should include: purpose summary, testing evidence (command outputs or screenshots), and notes on mkdocs or plugin manifest updates. Request review from another agent before merging to keep plugin
  behaviour predictable.

  ## Agent Execution Tips
  When automating tasks, prefer Gradle and mkdox CLIs over bespoke scripts, and document any new automation in `README.md` so future maintainers understand the workflow.

