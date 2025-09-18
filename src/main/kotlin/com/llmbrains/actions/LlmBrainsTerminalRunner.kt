package com.llmbrains.actions

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

interface LlmBrainsTerminalRunner {
    fun run(command: String, tabTitle: String)

    companion object {
        fun forProject(project: Project): LlmBrainsTerminalRunner = IdeaTerminalRunner(project)
    }
}

private class IdeaTerminalRunner(
    private val project: Project,
) : LlmBrainsTerminalRunner {

    override fun run(command: String, tabTitle: String) {
        val workingDirectory = project.basePath ?: System.getProperty("user.home")
        try {
            val terminalViewClass = Class.forName("com.intellij.terminal.TerminalView")
            val instance = terminalViewClass
                .getMethod("getInstance", Project::class.java)
                .invoke(null, project)
            val widget = terminalViewClass
                .getMethod("createLocalShellWidget", String::class.java, String::class.java)
                .invoke(instance, workingDirectory, tabTitle)
            widget.javaClass
                .getMethod("executeCommand", String::class.java)
                .invoke(widget, command)
        } catch (throwable: Throwable) {
            LOGGER.warn("Failed to run command in IDE terminal", throwable)
        }
    }

    companion object {
        private val LOGGER: Logger = Logger.getInstance(IdeaTerminalRunner::class.java)
    }
}
