package com.mkdox.services

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.openapi.diagnostic.Logger

object DockerService {
    private val logger = Logger.getInstance(DockerService::class.java)

    fun isDockerRunning(): Boolean {
        val commandLine = GeneralCommandLine("/bin/bash", "-lc", "docker info > /dev/null 2>&1")
        return try {
            val handler = CapturingProcessHandler(commandLine)
            val output = handler.runProcess(5_000)
            output.exitCode == 0
        } catch (exception: Exception) {
            logger.warn("Docker check failed", exception)
            false
        }
    }
}
