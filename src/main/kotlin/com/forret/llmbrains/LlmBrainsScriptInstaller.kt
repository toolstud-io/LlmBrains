package com.forret.llmbrains

import com.intellij.openapi.application.PathManager
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object LlmBrainsScriptInstaller {
    private const val BASH_SCRIPT_NAME = "llmbrains.sh"
    private const val POWERSHELL_SCRIPT_NAME = "llmbrains.ps1"
    private val installedScripts: Pair<Path, Path> by lazy { installScripts() }

    fun bashScriptPath(): Path = installedScripts.first
    fun powershellScriptPath(): Path = installedScripts.second

    private fun installScripts(): Pair<Path, Path> {
        val configDir = Path.of(PathManager.getConfigPath(), "llmbrains")
        Files.createDirectories(configDir)

        val bashTarget = configDir.resolve(BASH_SCRIPT_NAME)
        val bashResource = LlmBrainsScriptInstaller::class.java.getResourceAsStream("/scripts/$BASH_SCRIPT_NAME")
            ?: throw IOException("Missing resource /scripts/$BASH_SCRIPT_NAME")
        bashResource.use { input ->
            Files.copy(input, bashTarget, StandardCopyOption.REPLACE_EXISTING)
        }
        bashTarget.toFile().setExecutable(true, false)

        val ps1Target = configDir.resolve(POWERSHELL_SCRIPT_NAME)
        val ps1Resource = LlmBrainsScriptInstaller::class.java.getResourceAsStream("/scripts/$POWERSHELL_SCRIPT_NAME")
            ?: throw IOException("Missing resource /scripts/$POWERSHELL_SCRIPT_NAME")
        ps1Resource.use { input ->
            Files.copy(input, ps1Target, StandardCopyOption.REPLACE_EXISTING)
        }

        return Pair(bashTarget, ps1Target)
    }
}
