package com.mkdox.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.JBUI
import com.mkdox.services.DockerService
import com.mkdox.services.MkDoxStateService
import java.awt.Component
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JPanel

class MkDoxCheckAction : AnAction("Check mkdox"), DumbAware {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val state = MkDoxStateService.projectState(project)
        val dockerRunning = DockerService.isDockerRunning()

        val panel = checklistPanel(
            state = state,
            dockerRunning = dockerRunning,
        )

        DialogBuilder(project).apply {
            setTitle("Check mkdox")
            setCenterPanel(panel)
            addOkAction()
        }.show()
    }

    private fun checklistPanel(state: MkDoxStateService.State, dockerRunning: Boolean): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.border = JBUI.Borders.empty(12)

        val items = listOf(
            checkbox("Has docs directory (/docs)", state.hasDocsDirectory),
            checkbox("Has mkdocs.yml in project root", state.hasMkdocsFile),
            checkbox("Has docs/blog directory", state.hasBlogDirectory),
            checkbox("mkdox.sh available on PATH", state.hasScript),
            checkbox("Docker is running", dockerRunning),
        )

        items.forEachIndexed { index, checkBox ->
            if (index > 0) {
                panel.add(Box.createVerticalStrut(4))
            }
            panel.add(checkBox)
        }
        return panel
    }

    private fun checkbox(label: String, value: Boolean): JBCheckBox =
        JBCheckBox(label, value).apply {
            isEnabled = false
            alignmentX = Component.LEFT_ALIGNMENT
        }
}
