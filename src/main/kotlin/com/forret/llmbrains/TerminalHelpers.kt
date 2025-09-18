package com.forret.llmbrains

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.TerminalView

object TerminalHelpers {
    fun openAndRun(project: Project, title: String, command: String) {
        val terminalView = TerminalView.getInstance(project)
        val widget = terminalView.createLocalShellWidget(project.basePath ?: "", title)
        // Send the command and a newline so it executes immediately
        widget.executeCommand(command)
    }
}
