package com.forret.llmbrains

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import java.awt.Component
import java.awt.BorderLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel

class AgentSettingsConfigurable : Configurable {
    private val checkboxes: Map<String, JBCheckBox> = CodingAgents.all.associate { agent ->
        agent.id to JBCheckBox(agent.name)
    }

    private val panel: JComponent by lazy {
        JPanel(BorderLayout()).apply {
            val content = JPanel()
            content.layout = BoxLayout(content, BoxLayout.Y_AXIS)
            content.add(JBLabel("Select which coding agents should appear in the toolbar dropdown."))
            content.add(Box.createVerticalStrut(8))
            checkboxes.values.forEach { checkBox ->
                checkBox.alignmentX = Component.LEFT_ALIGNMENT
                content.add(checkBox)
            }
            add(content, BorderLayout.NORTH)
        }
    }

    override fun createComponent(): JComponent {
        reset()
        return panel
    }

    override fun isModified(): Boolean {
        val settings = AgentSettingsState.getInstance()
        return CodingAgents.all.any { agent ->
            checkboxes[agent.id]?.isSelected != settings.isAgentActive(agent.id)
        }
    }

    override fun apply() {
        val settings = AgentSettingsState.getInstance()
        CodingAgents.all.forEach { agent ->
            checkboxes[agent.id]?.let { checkBox ->
                settings.setAgentActive(agent.id, checkBox.isSelected)
            }
        }
    }

    override fun reset() {
        val settings = AgentSettingsState.getInstance()
        CodingAgents.all.forEach { agent ->
            checkboxes[agent.id]?.isSelected = settings.isAgentActive(agent.id)
        }
    }

    override fun getDisplayName(): String = "LLM Brains"
}
