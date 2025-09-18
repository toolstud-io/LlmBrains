package com.mkdox.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.mkdox.services.DockerService
import com.mkdox.services.MkDoxCommandExecutor
import com.mkdox.services.MkDoxNotifier
import com.mkdox.services.MkDoxStateService
import java.nio.file.Path
import java.nio.file.Paths
import org.jetbrains.plugins.terminal.TerminalView

class MkDoxServeAction : AnAction("Serve mkdox"), DumbAware {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val basePath = project.baseDirPath() ?: return

        val state = MkDoxStateService.projectState(project)
        if (!state.isReady) {
            MkDoxNotifier.warn(project, "mkdox is not configured yet.")
            return
        }

        if (!DockerService.isDockerRunning()) {
            MkDoxNotifier.warn(project, "Docker is not running. Start Docker to serve mkdox.")
            return
        }

        val command = serveCommand(basePath)
        if (!launchTerminal(project, basePath, command)) {
            val result = MkDoxCommandExecutor.execute(command, basePath)
            if (result.success) {
                MkDoxNotifier.info(project, "mkdox serve started in background.")
            } else {
                MkDoxNotifier.error(project, result.message)
            }
        }
    }

    private fun serveCommand(basePath: Path): String {
        val script = basePath.resolve("mkdox.sh")
        return if (java.nio.file.Files.isExecutable(script)) "./mkdox.sh serve" else "mkdox serve"
    }

    private fun launchTerminal(project: Project, basePath: Path, command: String): Boolean =
        try {
            val terminalView = TerminalView.getInstance(project)
            val widget = terminalView.createLocalShellWidget(basePath.toString(), "mkdox")
            ApplicationManager.getApplication().invokeLater {
                widget.executeCommand(command)
            }
            true
        } catch (throwable: Throwable) {
            MkDoxNotifier.warn(project, "Unable to open terminal: ${throwable.message ?: "unknown error"}.")
            false
        }

    private fun Project.baseDirPath(): Path? =
        basePath?.let(Paths::get)
}
