package com.mkdox.services

import com.intellij.openapi.project.Project
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit

object MkDoxStateService {
    data class State(
        val hasDocsDirectory: Boolean,
        val hasMkdocsFile: Boolean,
        val hasBlogDirectory: Boolean,
        val hasScript: Boolean,
    ) {
        val isReady: Boolean = hasDocsDirectory && hasMkdocsFile && hasScript
    }

    @Volatile
    internal var pathOverrideForWhich: String? = null

    fun projectState(project: Project): State =
        stateForPath(project.basePath?.let(Path::of))

    fun stateForPath(basePath: Path?): State {
        if (basePath == null) return State(false, false, false, false)
        val docsDir = Files.isDirectory(basePath.resolve("docs"))
        val mkdocsFile = hasMkdocsFile(basePath)
        val blogDir = Files.isDirectory(basePath.resolve("docs/blog"))
        val script = isScriptOnPath()
        return State(docsDir, mkdocsFile, blogDir, script)
    }

    private fun hasMkdocsFile(basePath: Path): Boolean =
        Files.isRegularFile(basePath.resolve("mkdocs.yml")) || Files.isRegularFile(basePath.resolve(".mkdocs.yml"))

    private fun isScriptOnPath(): Boolean {
        return try {
            val processBuilder = ProcessBuilder("which", MKDOX_SCRIPT_NAME)
            applyPathOverride(processBuilder)
            val process = processBuilder.start()
            if (!process.waitFor(1, TimeUnit.SECONDS)) {
                process.destroyForcibly()
                false
            } else {
                process.exitValue() == 0
            }
        } catch (ignore: InterruptedException) {
            Thread.currentThread().interrupt()
            false
        } catch (ignore: IOException) {
            false
        }
    }

    private fun applyPathOverride(processBuilder: ProcessBuilder) {
        val override = pathOverrideForWhich ?: return
        val environment = processBuilder.environment()
        val delimiter = File.pathSeparator
        val currentPath = environment[PATH_ENV] ?: System.getenv(PATH_ENV).orEmpty()
        environment[PATH_ENV] =
            if (currentPath.isBlank()) override else "$override$delimiter$currentPath"
    }

    private const val MKDOX_SCRIPT_NAME = "mkdox.sh"
    private const val PATH_ENV = "PATH"
}
