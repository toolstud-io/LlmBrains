package com.forret.llmbrains

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

object TerminalCommandRunner {
    fun run(project: Project, title: String, command: String) {
        val terminalManager = TerminalToolWindowManager.getInstance(project)
        val widget = terminalManager.createLocalShellWidget(project.basePath ?: "", title)
        widget.executeCommand(command)
    }
}
