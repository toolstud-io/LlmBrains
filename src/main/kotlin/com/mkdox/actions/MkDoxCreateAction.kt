package com.mkdox.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.mkdox.services.DockerService
import com.mkdox.services.MkDoxCommandExecutor
import com.mkdox.services.MkDoxNotifier
import com.mkdox.services.MkDoxStateService
import java.nio.file.Files
import java.nio.file.Path

class MkDoxCreateAction : AnAction("Create mkdox"), DumbAware {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val basePath = project.baseDirPath() ?: return

        if (MkDoxStateService.projectState(project).isReady) {
            MkDoxNotifier.info(project, "mkdox project already configured.")
            return
        }

        if (!DockerService.isDockerRunning()) {
            MkDoxNotifier.warn(project, "Docker is not running. Start Docker to initialise mkdox.")
            return
        }

        val projectName = project.name.ifBlank { basePath.fileName?.toString() ?: "project" }
        val command = "mkdox -E \"$projectName\" new ."

        object : Task.Backgroundable(project, "Initialising mkdox", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.text = "Running mkdox new"
                val result = MkDoxCommandExecutor.execute(command, basePath)
                if (result.success) {
                    MkDoxNotifier.info(project, result.message)
                    openMkdocsFile(project, basePath)
                } else {
                    MkDoxNotifier.error(project, result.message)
                }
            }
        }.queue()
    }

    private fun openMkdocsFile(project: Project, basePath: Path) {
        val candidate = sequenceOf("mkdocs.yml", ".mkdocs.yml")
            .map { basePath.resolve(it) }
            .firstOrNull { Files.exists(it) } ?: return

        ApplicationManager.getApplication().invokeLater {
            VirtualFileManager.getInstance().refreshAndFindFileByNioPath(candidate)?.let { virtualFile ->
                ApplicationManager.getApplication().invokeLater {
                    com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project).openFile(virtualFile, true)
                }
            }
        }
    }

    private fun Project.baseDirPath(): Path? =
        basePath?.let(Path::of)
}
