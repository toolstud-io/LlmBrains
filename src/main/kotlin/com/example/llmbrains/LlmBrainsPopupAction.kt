package com.example.llmbrains

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.popup.JBPopupFactory

class LlmBrainsPopupAction : AnAction("🫴", "Open any CLI coding agent in a new terminal window.", null), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val popupGroup = DefaultActionGroup().apply {
            add(SimpleRunAction("🫴 Claude Code (Anthropic)") { TerminalHelpers.openAndRun(project, "🫴 Claude", "claude") })
            add(SimpleRunAction("🫴 Codex CLI (OpenAI)") { TerminalHelpers.openAndRun(project, "🫴 Codex", "codex") })
            add(SimpleRunAction("🫴 Gemini CLI (Google)") { TerminalHelpers.openAndRun(project, "🫴 Gemini", "gemini") })
            addSeparator()
            add(SimpleRunAction("❓ Check what's installed") {
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
            clear
            # Print a colored title
            echo "Checking CLI coding agents..."; echo

            # Helper: show Installation section from docs/agents/*.md if available
            show_install() {
              doc_path="${'$'}1"
              if [ -f "${'$'}doc_path" ]; then
                echo
                echo "📘 Installation instructions from ${'$'}doc_path:"; echo
                # Extract lines from "## Installation" up to the next "##" header (or end of file)
                awk 'found{if($0 ~ /^## / && !first){exit} print} /^## Installation/{found=1; first=0}' "${'$'}doc_path"
                echo
              else
                echo "(Documentation not found at ${'$'}doc_path)"
              fi
            }

            # Checker: verifies command and prints version or install instructions
            check() {
              name="${'$'}1"; cmd="${'$'}2"; verFlag="${'$'}3"; doc="${'$'}4";
              if command -v "${'$'}cmd" >/dev/null 2>&1; then
                if [ -n "${'$'}verFlag" ]; then
                  echo "✅ ${'$'}name installed: ${'$'}( ${'$'}cmd ${'$'}verFlag 2>/dev/null | head -n 1 )"
                else
                  echo "✅ ${'$'}name installed: ${'$'}cmd"
                fi
              else
                echo "❌ ${'$'}name not found. See installation instructions below:"
                show_install "${'$'}doc"
              fi
            }

            # Perform checks with local docs references
            check "Claude" "claude" "-v" "docs/agents/claude.md";
            check "Codex" "codex" "-V" "docs/agents/codex.md";
            check "Gemini" "gemini" "-v" "docs/agents/gemini.md";
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
