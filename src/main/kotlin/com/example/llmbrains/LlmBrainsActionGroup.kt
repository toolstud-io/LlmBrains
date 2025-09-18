package com.example.llmbrains

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAware

class LlmBrainsActionGroup : ActionGroup("LLM Brains", "Open any CLI coding agent in a new terminal window.", null), DumbAware {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project = e?.project
        val actions = mutableListOf<AnAction>()
        actions += SimpleRunAction("🫴 Claude (Anthropic)") { project?.let { TerminalHelpers.openAndRun(it, "\uD83E\uDEC4 Claude", "claude") } }
        actions += SimpleRunAction("🫴 Codex (OpenAI)")     { project?.let { TerminalHelpers.openAndRun(it, "\uD83E\uDEC4 Codex", "codex") } }
        actions += SimpleRunAction("🫴 Gemini (Google)")    { project?.let { TerminalHelpers.openAndRun(it, "\uD83E\uDEC4 Gemini", "gemini") } }
        actions += SimpleRunAction("🫴 Qodo Command")       { project?.let { TerminalHelpers.openAndRun(it, "\uD83E\uDEC4 Qodo", "qodo") } }
        actions += Separator.getInstance()
        actions += SimpleRunAction("❓ Check what's installed") { project?.let { TerminalHelpers.openAndRun(it, "\uD83E\uDEC4 Check", buildCheckScript()) } }
        return actions.toTypedArray()
    }

    private fun buildCheckScript(): String {
        // Compact bash script to check presence and versions.
        // NOTE: We must escape all "$" occurrences inside the Kotlin string using ${'$'} to avoid template interpolation.
        val script = """
            bash -lc '
            check() {
              name="${'$'}1"; cmd="${'$'}2"; verFlag="${'$'}3"; install="${'$'}4";
              if command -v "${'$'}cmd" >/dev/null 2>&1; then
                if [ -n "${'$'}verFlag" ]; then
                  echo "✅  ${'$'}name installed: ${'$'}( ${'$'}cmd ${'$'}verFlag 2>/dev/null | head -n 1 )"
                else
                  echo "✅  ${'$'}name installed: ${'$'}cmd"
                fi
              else
                echo "❌  ${'$'}name not found. Install: ${'$'}install"
              fi
            }
            clear
            echo "Checking CLI coding agents..."; echo
            check "Claude" "claude" "--version" "npm install -g @anthropic-ai/claude-code";
            check "Codex" "codex" "--version" "npm install -g @openai/codex";
            check "Gemini" "gemini" "--version" "npm install -g @google/gemini-cli";
            check "Qodo" "qodo" "--version" "npm install -g @qodo/command";
            echo
            '
        """.trimIndent()
        return script
    }

    private class SimpleRunAction(
        text: String,
        val runner: () -> Unit
    ) : AnAction(text), DumbAware {
        override fun actionPerformed(e: AnActionEvent) = runner()
    }
}
