package com.forret.llmbrains

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.labels.LinkLabel
import com.intellij.ui.components.labels.LinkListener
import java.awt.BorderLayout
import java.awt.Component
import java.net.URI
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSeparator
import com.intellij.ui.components.JBTextField

class AgentSettingsConfigurable : Configurable {
    private val checkboxes: Map<String, JBCheckBox> = CodingAgents.all.associate { agent ->
        agent.id to JBCheckBox(agent.name)
    }

    // Custom agent form fields
    private val customEnabledCheckbox = JBCheckBox("Enable custom agent")
    private val customNameField = JBTextField()
    private val customCommandField = JBTextField()
    private val customUrlField = JBTextField()

    private val panel: JComponent by lazy {
        JPanel(BorderLayout()).apply {
            val content = JPanel()
            content.layout = BoxLayout(content, BoxLayout.Y_AXIS)
            content.add(JBLabel("Select which coding agents should appear in the toolbar dropdown."))
            content.add(Box.createVerticalStrut(8))
            CodingAgents.all.forEach { agent ->
                val row = JPanel()
                row.layout = BoxLayout(row, BoxLayout.X_AXIS)
                val checkBox = checkboxes[agent.id]!!
                checkBox.alignmentX = Component.LEFT_ALIGNMENT
                row.add(checkBox)
                row.add(Box.createHorizontalStrut(8))
                val domain = extractDomain(agent.url)
                val link = LinkLabel<Any>(domain, null)
                link.setListener(LinkListener { _, _ -> BrowserUtil.browse(agent.url) }, null)
                row.add(link)
                row.alignmentX = Component.LEFT_ALIGNMENT
                content.add(row)
            }

            // Custom agent section
            content.add(Box.createVerticalStrut(16))
            content.add(JSeparator())
            content.add(Box.createVerticalStrut(8))
            content.add(JBLabel("Custom Agent"))
            content.add(Box.createVerticalStrut(4))

            // Enabled checkbox
            customEnabledCheckbox.alignmentX = Component.LEFT_ALIGNMENT
            content.add(customEnabledCheckbox)
            content.add(Box.createVerticalStrut(4))

            // Name row
            val nameRow = JPanel()
            nameRow.layout = BoxLayout(nameRow, BoxLayout.X_AXIS)
            nameRow.add(JBLabel("Name: "))
            nameRow.add(Box.createHorizontalStrut(4))
            nameRow.add(customNameField)
            nameRow.alignmentX = Component.LEFT_ALIGNMENT
            content.add(nameRow)
            content.add(Box.createVerticalStrut(4))

            // Command row
            val commandRow = JPanel()
            commandRow.layout = BoxLayout(commandRow, BoxLayout.X_AXIS)
            commandRow.add(JBLabel("Command: "))
            commandRow.add(Box.createHorizontalStrut(4))
            commandRow.add(customCommandField)
            commandRow.alignmentX = Component.LEFT_ALIGNMENT
            content.add(commandRow)
            content.add(Box.createVerticalStrut(4))

            // URL row
            val urlRow = JPanel()
            urlRow.layout = BoxLayout(urlRow, BoxLayout.X_AXIS)
            urlRow.add(JBLabel("URL: "))
            urlRow.add(Box.createHorizontalStrut(4))
            urlRow.add(customUrlField)
            urlRow.alignmentX = Component.LEFT_ALIGNMENT
            content.add(urlRow)

            add(content, BorderLayout.NORTH)
        }
    }

    private fun extractDomain(url: String): String = try {
        URI(url).host?.removePrefix("www.") ?: url
    } catch (_: Exception) {
        url
    }

    override fun createComponent(): JComponent {
        reset()
        return panel
    }

    override fun isModified(): Boolean {
        val settings = AgentSettingsState.getInstance()
        val builtInModified = CodingAgents.all.any { agent ->
            checkboxes[agent.id]?.isSelected != settings.isAgentActive(agent.id)
        }
        val state = settings.getState()
        val customModified = customEnabledCheckbox.isSelected != state.customAgentEnabled ||
            customNameField.text != state.customAgentName ||
            customCommandField.text != state.customAgentCommand ||
            customUrlField.text != state.customAgentUrl
        return builtInModified || customModified
    }

    override fun apply() {
        val settings = AgentSettingsState.getInstance()
        CodingAgents.all.forEach { agent ->
            checkboxes[agent.id]?.let { checkBox ->
                settings.setAgentActive(agent.id, checkBox.isSelected)
            }
        }
        val state = settings.getState()
        state.customAgentEnabled = customEnabledCheckbox.isSelected
        state.customAgentName = customNameField.text
        state.customAgentCommand = customCommandField.text
        state.customAgentUrl = customUrlField.text
    }

    override fun reset() {
        val settings = AgentSettingsState.getInstance()
        CodingAgents.all.forEach { agent ->
            checkboxes[agent.id]?.isSelected = settings.isAgentActive(agent.id)
        }
        val state = settings.getState()
        customEnabledCheckbox.isSelected = state.customAgentEnabled
        customNameField.text = state.customAgentName
        customCommandField.text = state.customAgentCommand
        customUrlField.text = state.customAgentUrl
    }

    override fun getDisplayName(): String = "LLM Brains"
}
