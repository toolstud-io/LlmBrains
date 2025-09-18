package com.llmbrains.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class LlmBrainsAgentsActionGroup : ActionGroup(), DumbAware {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> = ACTIONS

    override fun update(e: AnActionEvent) {
        e.presentation.apply {
            text = "LLM Brains"
            description = "Open any CLI coding agent in a new terminal window."
            icon = ICON
        }
    }

    override fun isPopup(): Boolean = true

    companion object {
        private val ICON: Icon = IconLoader.getIcon("/icons/llmbrains.svg", LlmBrainsAgentsActionGroup::class.java)

        private val ACTIONS: Array<AnAction> = arrayOf(
            LlmBrainsCliAction(
                text = "Claude (Anthropic)",
                description = "Run the claude CLI in a dedicated terminal tab",
                commandProvider = LlmBrainsCommandPresets::claude,
            ),
            LlmBrainsCliAction(
                text = "Codex (OpenAI)",
                description = "Run the codex CLI in a dedicated terminal tab",
                commandProvider = LlmBrainsCommandPresets::codex,
            ),
            LlmBrainsCliAction(
                text = "Gemini (Google)",
                description = "Run the gemini CLI in a dedicated terminal tab",
                commandProvider = LlmBrainsCommandPresets::gemini,
            ),
            LlmBrainsCliAction(
                text = "Check what's installed",
                description = "Inspect which supported CLIs are available locally",
                commandProvider = LlmBrainsCommandPresets::diagnostic,
            ),
        )
    }
}
