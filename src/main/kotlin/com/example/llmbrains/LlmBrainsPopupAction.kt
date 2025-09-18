package com.example.llmbrains

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.popup.JBPopupFactory

class LlmBrainsPopupAction : AnAction("🫴", "Open any CLI coding agent in a new terminal window.", null), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val popupGroup = DefaultActionGroup().apply {
            add(SimpleRunAction("Claude (Anthropic)") { TerminalHelpers.openAndRun(project, "🫴 Claude", "claude") })
            add(SimpleRunAction("Codex (OpenAI)") { TerminalHelpers.openAndRun(project, "🫴 Codex", "codex") })
            add(SimpleRunAction("Gemini (Google)") { TerminalHelpers.openAndRun(project, "🫴 Gemini", "gemini") })
            addSeparator()
            add(SimpleRunAction("Check what's installed") {
                TerminalHelpers.openAndRun(project, "🫴 Check", buildCheckScript())
            })
        }

        val context = e.dataContext
        val popup = JBPopupFactory.getInstance().createActionGroupPopup(
            "LLM Brains",
            popupGroup,
            context,
            JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
            true
        )
        popup.showInBestPositionFor(context)
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
                  echo "✅ ${'$'}name installed: ${'$'}( ${'$'}cmd ${'$'}verFlag 2>/dev/null | head -n 1 )"
                else
                  echo "✅ ${'$'}name installed: ${'$'}cmd"
                fi
              else
                echo "❌ ${'$'}name not found. Install: ${'$'}install"
              fi
            }
            echo "Checking CLI coding agents..."; echo
            check "Claude" "claude" "--version" "https://docs.anthropic.com/claude/docs/claude-code";
            check "Codex" "codex" "--version" "https://platform.openai.com";
            check "Gemini" "gemini" "--version" "https://ai.google.dev/gemini-api/docs";
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
