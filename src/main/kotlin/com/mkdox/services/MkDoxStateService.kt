package com.mkdox.services

import com.intellij.openapi.project.Project
import java.nio.file.Files
import java.nio.file.Path

object MkDoxStateService {
    data class State(
        val hasDocsDirectory: Boolean,
        val hasMkdocsFile: Boolean,
        val hasScript: Boolean,
    ) {
        val isReady: Boolean = hasDocsDirectory && hasMkdocsFile && hasScript
    }

    fun projectState(project: Project): State =
        stateForPath(project.basePath?.let(Path::of))

    fun stateForPath(basePath: Path?): State {
        if (basePath == null) return State(false, false, false)
        val docsDir = Files.isDirectory(basePath.resolve("docs"))
        val mkdocsFile = hasMkdocsFile(basePath)
        val script = isExecutable(basePath.resolve("mkdox.sh"))
        return State(docsDir, mkdocsFile, script)
    }

    private fun hasMkdocsFile(basePath: Path): Boolean =
        Files.isRegularFile(basePath.resolve("mkdocs.yml")) || Files.isRegularFile(basePath.resolve(".mkdocs.yml"))

    private fun isExecutable(path: Path): Boolean =
        Files.isRegularFile(path) && Files.isExecutable(path)
}
