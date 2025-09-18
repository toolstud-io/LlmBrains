package com.mkdox.services

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.diagnostic.Logger
import java.nio.file.Path

object MkDoxCommandExecutor {
    private val logger = Logger.getInstance(MkDoxCommandExecutor::class.java)
    private const val TIMEOUT_SECONDS: Long = 120

    data class Result(val success: Boolean, val message: String)

    fun execute(shellCommand: String, workingDirectory: Path): Result {
        val commandLine = GeneralCommandLine("/bin/bash", "-lc", shellCommand)
            .withEnvironment("TERM", System.getenv("TERM") ?: "xterm-256color")
            .withWorkDirectory(workingDirectory.toFile())

        return try {
            val handler = CapturingProcessHandler(commandLine)
            val output: ProcessOutput = handler.runProcess((TIMEOUT_SECONDS * 1000).toInt())
            if (output.exitCode == 0) {
                Result(true, output.stdout.ifBlank { "Command completed successfully." })
            } else {
                val message = buildString {
                    appendLine("mkdox command failed (${output.exitCode}).")
                    if (output.stdout.isNotBlank()) appendLine(output.stdout.trim())
                    if (output.stderr.isNotBlank()) appendLine(output.stderr.trim())
                }.trim()
                Result(false, message)
            }
        } catch (exception: Exception) {
            logger.warn("Failed to run command '$shellCommand'", exception)
            Result(false, exception.message ?: "Unknown error running mkdox command")
        }
    }
}
