package com.forret.llmbrains

import com.intellij.openapi.application.PathManager
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object LlmBrainsScriptInstaller {
    private const val SCRIPT_NAME = "llmbrains.sh"
    private const val RESOURCE_PATH = "/scripts/" + SCRIPT_NAME
    private val installedScript: Path by lazy { installScript() }

    fun scriptPath(): Path = installedScript

    private fun installScript(): Path {
        val configDir = Path.of(PathManager.getConfigPath(), "llmbrains")
        Files.createDirectories(configDir)
        val target = configDir.resolve(SCRIPT_NAME)
        val resource = LlmBrainsScriptInstaller::class.java.getResourceAsStream(RESOURCE_PATH)
            ?: throw IOException("Missing resource $RESOURCE_PATH")
        resource.use { input ->
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING)
        }
        target.toFile().setExecutable(true, false)
        return target
    }
}
