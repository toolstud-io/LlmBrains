package com.llmbrains.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project

class LlmBrainsCliAction(
    text: String,
    description: String,
    private val commandProvider: () -> LlmBrainsTerminalCommand,
    private val runnerFactory: (Project) -> LlmBrainsTerminalRunner = LlmBrainsTerminalRunner.Companion::forProject,
) : DumbAwareAction(text, description, null) {

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        runWithProject(project)
    }

    internal fun runWithProject(project: Project) {
        val command = commandProvider.invoke()
        runnerFactory.invoke(project).run(command.command, command.tabTitle)
    }
}
