package com.forret.llmbrains

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.TerminalService

object TerminalCommandRunner {
    fun run(project: Project, title: String, command: String) {
        val terminalService = TerminalService.getInstance(project)
        val widget = terminalService.createLocalShellWidget(project.basePath ?: "", title)
        widget.executeCommand(command)
    }
}
