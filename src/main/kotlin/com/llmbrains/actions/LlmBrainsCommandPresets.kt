package com.llmbrains.actions

object LlmBrainsCommandPresets {
    fun claude(): LlmBrainsTerminalCommand = LlmBrainsTerminalCommand(
        tabTitle = "🫴 Claude",
        command = "claude",
    )

    fun codex(): LlmBrainsTerminalCommand = LlmBrainsTerminalCommand(
        tabTitle = "🫴 Codex",
        command = "codex",
    )

    fun gemini(): LlmBrainsTerminalCommand = LlmBrainsTerminalCommand(
        tabTitle = "🫴 Gemini",
        command = "gemini",
    )

    fun diagnostic(): LlmBrainsTerminalCommand = LlmBrainsTerminalCommand(
        tabTitle = "🫴 Check",
        command = buildDiagnosticCommand(),
    )

    private fun buildDiagnosticCommand(): String {
        val script = """
            set -euo pipefail

            check_cli() {
              local name="${'$'}1"
              local version_cmd="${'$'}2"
              local install_hint="${'$'}3"

              if command -v "${'$'}name" >/dev/null 2>&1; then
                echo "[OK] ${'$'}name is installed."
                if [ -n "${'$'}version_cmd" ]; then
                  eval "${'$'}version_cmd" || echo "[WARN] unable to run: ${'$'}version_cmd"
                fi
              else
                echo "[MISSING] ${'$'}name is not installed."
                echo "         Install hint: ${'$'}install_hint"
              fi
              echo
            }

            check_cli "claude" "claude --version" "https://docs.anthropic.com/claude/docs/claude-code#getting-started"
            check_cli "codex" "codex --version" "https://github.com/pforret/codex"
            check_cli "gemini" "gemini --version" "https://ai.google.dev/gemini-api/docs/ai-studio-cli"
        """.trimIndent()

        return "bash -lc '${script.escapeForSingleQuotes()}'"
    }

    private fun String.escapeForSingleQuotes(): String = replace("'", "'\"'\"'")
}
