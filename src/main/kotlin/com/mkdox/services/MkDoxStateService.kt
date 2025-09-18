package com.mkdox.services

import com.intellij.openapi.project.Project
import java.nio.file.Files
import java.nio.file.Path

object MkDoxStateService {
    data class State(
        val hasDocsDirectory: Boolean,
        val hasMkdocsFile: Boolean,
        val hasBlogDirectory: Boolean,
    ) {
        val isReady: Boolean = hasDocsDirectory && hasMkdocsFile && hasBlogDirectory
    }

    fun projectState(project: Project): State =
        stateForPath(project.basePath?.let(Path::of))

    fun stateForPath(basePath: Path?): State {
        if (basePath == null) return State(false, false, false)
        val docsDir = Files.isDirectory(basePath.resolve("docs"))
        val mkdocsFile = hasMkdocsFile(basePath)
        val blogDir = Files.isDirectory(basePath.resolve("docs/blog"))
        return State(docsDir, mkdocsFile, blogDir)
    }

    private fun hasMkdocsFile(basePath: Path): Boolean =
        Files.isRegularFile(basePath.resolve("mkdocs.yml")) || Files.isRegularFile(basePath.resolve(".mkdocs.yml"))
}
