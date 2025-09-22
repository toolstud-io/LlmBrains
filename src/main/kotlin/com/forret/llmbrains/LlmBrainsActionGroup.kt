package com.forret.llmbrains

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.project.DumbAware

class LlmBrainsActionGroup : ActionGroup("LLM Brains", "Open any CLI coding agent in a new terminal window.", null), DumbAware {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project = e?.project
        val actions = mutableListOf<AnAction>()
        val settings = AgentSettingsState.getInstance()
        val activeAgents = settings.activeAgents()
        activeAgents.forEach { agent ->
            actions += SimpleRunAction(agent.dropdownLabel) {
                project?.let { TerminalCommandRunner.run(it, agent.name, agent.command) }
            }
        }
        if (activeAgents.isNotEmpty()) {
            actions += Separator.getInstance()
        }
        actions += SimpleRunAction("❓ Check agent versions") {
            project?.let { TerminalCommandRunner.run(it, "❓ Check Agents", buildCheckScript()) }
        }
        actions += SimpleRunAction("🔄 Update all agents") {
            project?.let { TerminalCommandRunner.run(it, "🔄️ Update Agents", buildUpdateScript(activeAgents)) }
        }
        return actions.toTypedArray()
    }

    private fun buildCheckScript(): String {
        val scriptPath = LlmBrainsScriptInstaller.scriptPath()
        val commands = CodingAgents.all.joinToString(separator = "\n") { agent ->
            val name = shellQuote(agent.name)
            val versionCommand = shellQuote(versionCommandFor(agent))
            val installHint = shellQuote(agent.installHint)
            "    llmbrains.sh check $name $versionCommand $installHint"
        }
        return """
            bash -lc '
            SCRIPT_PATH="${escapeForDoubleQuotes(scriptPath.toString())}"
            SCRIPT_DIR=$(dirname "${'$'}SCRIPT_PATH")
            PATH="${'$'}SCRIPT_DIR:${'$'}PATH"
            clear
            echo "Checking CLI coding agents..."; echo
$commands
            '
        """.trimIndent()
    }

    private fun buildUpdateScript(agents: List<CodingAgent>): String {
        if (agents.isEmpty()) {
            return """
                bash -lc 'echo "No coding agents are enabled. Enable them via Preferences > Tools > LLM Brains."'
            """.trimIndent()
        }
        val scriptPath = LlmBrainsScriptInstaller.scriptPath()
        val commands = agents.joinToString(separator = "\n") { agent ->
            val name = shellQuote(agent.name)
            val binary = shellQuote(agent.command)
            val updateHint = shellQuote(agent.updateHint)
            val installHint = shellQuote(agent.installHint)
            "    llmbrains.sh update $name $binary $updateHint $installHint"
        }
        return """
            bash -lc '
            SCRIPT_PATH="${escapeForDoubleQuotes(scriptPath.toString())}"
            SCRIPT_DIR=$(dirname "${'$'}SCRIPT_PATH")
            PATH="${'$'}SCRIPT_DIR:${'$'}PATH"
            clear
            echo "Updating enabled coding agents..."; echo
$commands
            echo "All done."
            '
        """.trimIndent()
    }

    private fun versionCommandFor(agent: CodingAgent): String =
        listOf(agent.command, agent.versionArgs)
            .filter { it.isNotBlank() }
            .joinToString(" ")

    private fun shellQuote(value: String): String {
        val escaped = escapeForDoubleQuotes(value)
        return "\"$escaped\""
    }

    private fun escapeForDoubleQuotes(value: String): String {
        val builder = StringBuilder(value.length)
        value.forEach { char ->
            when (char) {
                '\\', '"', '$', '`' -> builder.append('\\').append(char)
                else -> builder.append(char)
            }
        }
        return builder.toString()
    }

    private class SimpleRunAction(
        text: String,
        val runner: () -> Unit
    ) : AnAction(text), DumbAware {
        override fun actionPerformed(e: AnActionEvent) = runner()
    }
}
