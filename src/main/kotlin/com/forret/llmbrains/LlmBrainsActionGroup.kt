package com.forret.llmbrains

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAware

class LlmBrainsActionGroup : ActionGroup("LLM Brains", "Open any CLI coding agent in a new terminal window.", null), DumbAware {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project = e?.project
        val actions = mutableListOf<AnAction>()
        actions += SimpleRunAction("🫴 Claude Code")   { project?.let { TerminalCommandRunner.run(it, "🫴 Claude", "claude") } }
        actions += SimpleRunAction("🫴 Codex CLI")     { project?.let { TerminalCommandRunner.run(it, "🫴 Codex", "codex") } }
        actions += SimpleRunAction("🫴 Gemini CLI")    { project?.let { TerminalCommandRunner.run(it, "🫴 Gemini", "gemini") } }
        actions += SimpleRunAction("🫴 Qodo Command")  { project?.let { TerminalCommandRunner.run(it, "🫴 Qodo", "qodo") } }
        actions += Separator.getInstance()
        actions += SimpleRunAction("❓ Check what's installed") { project?.let { TerminalCommandRunner.run(it, "\uD83E\uDEC4 Check", buildCheckScript()) } }
        return actions.toTypedArray()
    }

    private fun buildCheckScript(): String {
        val script = """
            bash -lc '
            clear
            echo "Checking CLI coding agents..."; echo
            ./check_version.sh "Claude Code" "claude" "--version" "npm install -g @anthropic-ai/claude-code"
            ./check_version.sh "Codex CLI" "codex" "--version" "npm install -g @openai/codex"
            ./check_version.sh "Gemini CLI" "gemini" "--version" "npm install -g @google/gemini-cli"
            ./check_version.sh "Qodo Command" "qodo" "--version | grep Client" "npm install -g @qodo/command"
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
