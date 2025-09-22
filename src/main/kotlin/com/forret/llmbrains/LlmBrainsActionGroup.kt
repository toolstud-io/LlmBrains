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
        val agentChecks = CodingAgents.all.joinToString(separator = "\n") { agent ->
            "            check_version \"${agent.name}\"  \"${agent.command}\" \"${agent.versionArgs}\" \"${agent.installHint}\""
        }
        val dollar = "${'$'}"
        return """
            bash -lc '
            function check_version() {
                if command -v ${dollar}2 &> /dev/null; then
                    echo "- ${dollar}1 is installed:\033[0;32m ${dollar}(${dollar}2 ${dollar}3 2>&1) \033[0m"
                else
                    echo "( ${dollar}1 is NOT installed. You can install it with: ${dollar}4 )"
                fi
            }
            clear
            echo "Checking CLI coding agents..."; echo
$agentChecks
            '
        """.trimIndent()
    }

    private fun buildUpdateScript(agents: List<CodingAgent>): String {
        if (agents.isEmpty()) {
            return """
                bash -lc 'echo "No coding agents are enabled. Enable them via Preferences > Tools > LLM Brains."'
            """.trimIndent()
        }
        val dollar = "${'$'}"
        val updateCalls = agents.joinToString(separator = "\n") { agent ->
            "            update_agent \"${agent.name}\" \"${agent.command}\" \"${agent.updateHint}\""
        }
        return """
            bash -lc '
            function update_agent() {
                local name="${dollar}1"
                local command="${dollar}2"
                local hint="${dollar}3"
                if command -v "${dollar}command" &> /dev/null; then
                    echo "- Updating ${dollar}name..."
                    eval "${dollar}hint"
                else
                    echo "! ${dollar}name is not installed. Run: ${dollar}hint"
                fi
                echo
            }
            clear
            echo "Updating enabled coding agents..."; echo
$updateCalls
            echo "All done."
            '
        """.trimIndent()
    }

    private class SimpleRunAction(
        text: String,
        val runner: () -> Unit
    ) : AnAction(text), DumbAware {
        override fun actionPerformed(e: AnActionEvent) = runner()
    }
}
