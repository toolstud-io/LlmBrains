package com.forret.llmbrains

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.TerminalView

object TerminalCommandRunner {
    fun run(project: Project, title: String, command: String) {
        val terminalView = TerminalView.getInstance(project)
        val widget = terminalView.createLocalShellWidget(project.basePath ?: "", title)
        widget.executeCommand(command)
    }
}
