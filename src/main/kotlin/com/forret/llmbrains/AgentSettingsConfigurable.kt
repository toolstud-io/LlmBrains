package com.forret.llmbrains

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.TableUtil
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.labels.LinkLabel
import com.intellij.ui.components.labels.LinkListener
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.net.URI
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.DefaultCellEditor
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.JTable
import javax.swing.table.TableCellEditor

class AgentSettingsConfigurable : Configurable {
    private val checkboxes: Map<String, JBCheckBox> = CodingAgents.all.associate { agent ->
        agent.id to JBCheckBox(agent.name)
    }

    // Custom invocations table: agent + label + extra args + emoji, one row per dropdown entry
    private val variantsModel = ListTableModel<CustomVariantEntry>(AgentColumn, LabelColumn, ExtraArgsColumn, EmojiColumn)
    private val variantsTable = TableView(variantsModel).apply {
        setShowGrid(false)
        emptyText.text = "No custom invocations yet — click + to add one"
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

            // Custom invocations section
            content.add(Box.createVerticalStrut(16))
            content.add(JSeparator())
            content.add(Box.createVerticalStrut(8))
            content.add(JBLabel("Custom invocations"))
            content.add(
                JBLabel(
                    "Launch an agent with extra CLI parameters. Each row appears in the dropdown under its agent, " +
                        "only while that agent is enabled. Extra args are appended verbatim to the agent command.",
                ),
            )
            content.add(Box.createVerticalStrut(4))
            content.add(JBLabel("Syntax help:"))
            CodingAgents.withVariantHelp.forEach { agent ->
                val help = JBLabel(agent.variantHelp)
                help.font = Font(Font.MONOSPACED, Font.PLAIN, help.font.size - 1)
                help.alignmentX = Component.LEFT_ALIGNMENT
                content.add(help)
            }
            content.add(Box.createVerticalStrut(4))
            val tablePanel = ToolbarDecorator.createDecorator(variantsTable)
                .setAddAction {
                    TableUtil.stopEditing(variantsTable)
                    variantsModel.addRow(CustomVariantEntry())
                    val last = variantsModel.rowCount - 1
                    variantsTable.setRowSelectionInterval(last, last)
                    variantsTable.editCellAt(last, 1)
                }
                .setRemoveAction {
                    TableUtil.stopEditing(variantsTable)
                    TableUtil.removeSelectedItems(variantsTable)
                }
                .disableUpDownActions()
                .createPanel()
            tablePanel.alignmentX = Component.LEFT_ALIGNMENT
            tablePanel.preferredSize = Dimension(700, 160)
            tablePanel.maximumSize = Dimension(Int.MAX_VALUE, 240)
            content.add(tablePanel)

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

    private fun editedVariants(): List<CustomVariantEntry> = variantsModel.items.map { it.copy() }

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
        val variantsModified = editedVariants() != state.customVariants
        val customModified = customEnabledCheckbox.isSelected != state.customAgentEnabled ||
            customNameField.text != state.customAgentName ||
            customCommandField.text != state.customAgentCommand ||
            customUrlField.text != state.customAgentUrl
        return builtInModified || variantsModified || customModified
    }

    override fun apply() {
        TableUtil.stopEditing(variantsTable)
        val settings = AgentSettingsState.getInstance()
        CodingAgents.all.forEach { agent ->
            checkboxes[agent.id]?.let { checkBox ->
                settings.setAgentActive(agent.id, checkBox.isSelected)
            }
        }
        val state = settings.getState()
        state.customVariants = editedVariants().toMutableList()
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
        variantsModel.items = state.customVariants.map { it.copy() }
        customEnabledCheckbox.isSelected = state.customAgentEnabled
        customNameField.text = state.customAgentName
        customCommandField.text = state.customAgentCommand
        customUrlField.text = state.customAgentUrl
    }

    override fun getDisplayName(): String = "LLM Brains"

    private abstract class TextColumn(name: String) : ColumnInfo<CustomVariantEntry, String>(name) {
        override fun isCellEditable(item: CustomVariantEntry): Boolean = true
    }

    private object AgentColumn : TextColumn("Agent") {
        override fun valueOf(item: CustomVariantEntry): String = item.agentId

        override fun setValue(item: CustomVariantEntry, value: String) {
            item.agentId = value
        }

        override fun getEditor(item: CustomVariantEntry): TableCellEditor =
            DefaultCellEditor(ComboBox(CodingAgents.all.map { it.id }.toTypedArray()))

        override fun getWidth(table: JTable): Int = 110
    }

    private object LabelColumn : TextColumn("Label") {
        override fun valueOf(item: CustomVariantEntry): String = item.label

        override fun setValue(item: CustomVariantEntry, value: String) {
            item.label = value
        }

        override fun getWidth(table: JTable): Int = 160
    }

    private object ExtraArgsColumn : TextColumn("Extra args") {
        override fun valueOf(item: CustomVariantEntry): String = item.extraArgs

        override fun setValue(item: CustomVariantEntry, value: String) {
            item.extraArgs = value
        }
    }

    private object EmojiColumn : TextColumn("Emoji") {
        override fun valueOf(item: CustomVariantEntry): String = item.emoji

        override fun setValue(item: CustomVariantEntry, value: String) {
            item.emoji = value.trim().ifBlank { DEFAULT_VARIANT_EMOJI }
        }

        override fun getEditor(item: CustomVariantEntry): TableCellEditor =
            DefaultCellEditor(ComboBox(SUGGESTED_VARIANT_EMOJIS.toTypedArray()).apply { isEditable = true })

        override fun getWidth(table: JTable): Int = 70
    }
}
