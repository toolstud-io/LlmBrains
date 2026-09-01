package com.forret.llmbrains

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.labels.LinkLabel
import com.intellij.ui.components.labels.LinkListener
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.net.URI
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSeparator
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField

class AgentSettingsConfigurable : Configurable {
    private val checkboxes: Map<String, JBCheckBox> = CodingAgents.all.associate { agent ->
        agent.id to JBCheckBox(agent.name)
    }

    // Preset variant checkboxes (indented under their parent agent)
    private val variantCheckboxes: Map<String, JBCheckBox> = CodingAgents.presetVariants.associate { variant ->
        val command = CodingAgents.findById(variant.agentId)?.let { variant.commandFor(it) } ?: variant.extraArgs
        variant.id to JBCheckBox("${variant.label}  —  $command")
    }

    // Custom variants: one per line, "Label | extra args" or "agent-id | Label | extra args"
    private val customVariantsArea = JBTextArea(5, 60)

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

                val variantBoxes = CodingAgents.presetVariantsFor(agent.id).map { variantCheckboxes[it.id]!! }
                variantBoxes.forEach { variantBox ->
                    val variantRow = JPanel()
                    variantRow.layout = BoxLayout(variantRow, BoxLayout.X_AXIS)
                    variantRow.add(Box.createHorizontalStrut(24))
                    variantRow.add(variantBox)
                    variantRow.alignmentX = Component.LEFT_ALIGNMENT
                    content.add(variantRow)
                }
                if (variantBoxes.isNotEmpty()) {
                    // Variants only show in the dropdown when the parent agent is enabled
                    checkBox.addItemListener { variantBoxes.forEach { it.isEnabled = checkBox.isSelected } }
                }
            }

            // Custom variants section
            content.add(Box.createVerticalStrut(16))
            content.add(JSeparator())
            content.add(Box.createVerticalStrut(8))
            content.add(JBLabel("Custom variants — one per line: \"Label | extra args\" (Claude) or \"agent-id | Label | extra args\""))
            content.add(JBLabel("Agent ids: ${CodingAgents.all.joinToString(", ") { it.id }}"))
            content.add(Box.createVerticalStrut(4))
            val customVariantsScroll = JBScrollPane(customVariantsArea)
            customVariantsScroll.alignmentX = Component.LEFT_ALIGNMENT
            customVariantsScroll.maximumSize = Dimension(Int.MAX_VALUE, 120)
            content.add(customVariantsScroll)

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
        val variantsModified = CodingAgents.presetVariants.any { variant ->
            variantCheckboxes[variant.id]?.isSelected != settings.isVariantActive(variant)
        }
        val state = settings.getState()
        val customVariantsModified = customVariantsArea.text != state.customVariantLines
        val customModified = customEnabledCheckbox.isSelected != state.customAgentEnabled ||
            customNameField.text != state.customAgentName ||
            customCommandField.text != state.customAgentCommand ||
            customUrlField.text != state.customAgentUrl
        return builtInModified || variantsModified || customVariantsModified || customModified
    }

    override fun apply() {
        val settings = AgentSettingsState.getInstance()
        CodingAgents.all.forEach { agent ->
            checkboxes[agent.id]?.let { checkBox ->
                settings.setAgentActive(agent.id, checkBox.isSelected)
            }
        }
        CodingAgents.presetVariants.forEach { variant ->
            variantCheckboxes[variant.id]?.let { checkBox ->
                settings.setVariantActive(variant, checkBox.isSelected)
            }
        }
        val state = settings.getState()
        state.customVariantLines = customVariantsArea.text
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
        CodingAgents.presetVariants.forEach { variant ->
            variantCheckboxes[variant.id]?.let { checkBox ->
                checkBox.isSelected = settings.isVariantActive(variant)
                checkBox.isEnabled = settings.isAgentActive(variant.agentId)
            }
        }
        val state = settings.getState()
        customVariantsArea.text = state.customVariantLines
        customEnabledCheckbox.isSelected = state.customAgentEnabled
        customNameField.text = state.customAgentName
        customCommandField.text = state.customAgentCommand
        customUrlField.text = state.customAgentUrl
    }

    override fun getDisplayName(): String = "LLM Brains"
}
