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
