package com.forret.llmbrains

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.options.ShowSettingsUtil

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
        // Open IDE Settings > Tools > LLM Tools to enable/disable providers
        actions += Separator.getInstance()
        actions += SimpleRunAction("🤌 Enable/Disable agents") {
        // Settings | LLM Brains
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "LLM Brains")
        }
        actions += SimpleRunAction("👍 Check all versions") {
            project?.let { TerminalCommandRunner.run(it, "👍 Check Agents", buildCheckScript()) }
        }
        actions += SimpleRunAction("🤞 Update all agents") {
            project?.let { TerminalCommandRunner.run(it, "🤞 Update Agents", buildUpdateScript(activeAgents)) }
        }
        return actions.toTypedArray()
    }

    private fun buildCheckScript(): String {
        if (OsDetector.isWindows()) {
            return buildWindowsCheckScript()
        }
        val scriptPath = LlmBrainsScriptInstaller.bashScriptPath()
        val agentData = CodingAgents.all.joinToString(separator = "\n") { agent ->
            val name = escapeForDoubleQuotes(agent.name)
            val command = escapeForDoubleQuotes(agent.command)
            val versionArgs = escapeForDoubleQuotes(agent.versionArgs)
            val installHint = escapeForDoubleQuotes(agent.installHint)
            "$name|$command|$versionArgs|$installHint"
        }
        return """
            bash -lc '
            SCRIPT_PATH="${escapeForDoubleQuotes(scriptPath.toString())}"
            SCRIPT_DIR=$(dirname "${'$'}SCRIPT_PATH")
            PATH="${'$'}SCRIPT_DIR:${'$'}PATH"
            llmbrains.sh check-all "$agentData"
            '
        """.trimIndent()
    }

    private fun buildWindowsCheckScript(): String {
        val scriptPath = LlmBrainsScriptInstaller.powershellScriptPath()
        val agentData = CodingAgents.all.joinToString(separator = "\n") { agent ->
            val name = escapeForPowerShell(agent.name)
            val command = escapeForPowerShell(agent.command)
            val versionArgs = escapeForPowerShell(agent.versionArgs)
            val installHint = escapeForPowerShell(agent.installHint)
            "$name|$command|$versionArgs|$installHint"
        }
        val escapedPath = escapeForPowerShell(scriptPath.toString())
        val escapedData = escapeForPowerShell(agentData)
        return """powershell -NoProfile -Command "& '$escapedPath' check-all '$escapedData'""""
    }

    private fun buildUpdateScript(agents: List<CodingAgent>): String {
        if (OsDetector.isWindows()) {
            return buildWindowsUpdateScript(agents)
        }
        val scriptPath = LlmBrainsScriptInstaller.bashScriptPath()
        val agentData = CodingAgents.all.joinToString(separator = "\n") { agent ->
            val id = escapeForDoubleQuotes(agent.id)
            val name = escapeForDoubleQuotes(agent.name)
            val command = escapeForDoubleQuotes(agent.command)
            val versionArgs = escapeForDoubleQuotes(agent.versionArgs)
            val installHint = escapeForDoubleQuotes(agent.installHint)
            val updateHint = escapeForDoubleQuotes(agent.updateHint)
            "$id|$name|$command|$versionArgs|$installHint|$updateHint"
        }
        val activeIds = agents.joinToString(",") { it.id }
        return """
            bash -lc '
            SCRIPT_PATH="${escapeForDoubleQuotes(scriptPath.toString())}"
            SCRIPT_DIR=$(dirname "${'$'}SCRIPT_PATH")
            PATH="${'$'}SCRIPT_DIR:${'$'}PATH"
            llmbrains.sh update-all "$agentData" "$activeIds"
            '
        """.trimIndent()
    }

    private fun buildWindowsUpdateScript(agents: List<CodingAgent>): String {
        val scriptPath = LlmBrainsScriptInstaller.powershellScriptPath()
        val agentData = CodingAgents.all.joinToString(separator = "\n") { agent ->
            val id = escapeForPowerShell(agent.id)
            val name = escapeForPowerShell(agent.name)
            val command = escapeForPowerShell(agent.command)
            val versionArgs = escapeForPowerShell(agent.versionArgs)
            val installHint = escapeForPowerShell(agent.installHint)
            val updateHint = escapeForPowerShell(agent.updateHint)
            "$id|$name|$command|$versionArgs|$installHint|$updateHint"
        }
        val activeIds = agents.joinToString(",") { it.id }
        val escapedPath = escapeForPowerShell(scriptPath.toString())
        val escapedData = escapeForPowerShell(agentData)
        val escapedIds = escapeForPowerShell(activeIds)
        return """powershell -NoProfile -Command "& '$escapedPath' update-all '$escapedData' '$escapedIds'""""
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

    private fun escapeForPowerShell(value: String): String {
        return value.replace("'", "''")
    }

    private class SimpleRunAction(
        text: String,
        val runner: () -> Unit
    ) : AnAction(text), DumbAware {
        override fun actionPerformed(e: AnActionEvent) = runner()
    }
}
