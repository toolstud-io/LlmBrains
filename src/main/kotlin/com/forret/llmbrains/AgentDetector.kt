package com.forret.llmbrains

import java.io.IOException
import java.util.concurrent.TimeUnit

object AgentDetector {
    private const val TIMEOUT_MS = 5000L

    /**
     * Check if a command is available on the system.
     * Uses "command -v" on Unix or "where" on Windows.
     */
    fun isCommandAvailable(command: String): Boolean {
        return try {
            val process = if (OsDetector.isWindows()) {
                ProcessBuilder("where", command)
                    .redirectErrorStream(true)
                    .start()
            } else {
                // Use bash login shell to get full PATH
                ProcessBuilder("bash", "-lc", "command -v $command")
                    .redirectErrorStream(true)
                    .start()
            }

            val completed = process.waitFor(TIMEOUT_MS, TimeUnit.MILLISECONDS)
            if (!completed) {
                process.destroyForcibly()
                return false
            }

            process.exitValue() == 0
        } catch (e: IOException) {
            false
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            false
        }
    }

    /**
     * Detect all installed agents and return a map of agent ID to installed status.
     */
    fun detectAllAgents(): Map<String, Boolean> {
        return CodingAgents.all.associate { agent ->
            agent.id to isCommandAvailable(agent.command)
        }
    }

    /**
     * Auto-detect and configure agents.
     * Returns pair of (enabled count, total count).
     */
    fun autoDetectAndConfigure(): Pair<Int, Int> {
        val settings = AgentSettingsState.getInstance()

        // First, enable all agents
        settings.enableAllAgents()

        // Detect which commands are available
        val results = detectAllAgents()

        // Disable agents that are not installed
        var enabledCount = 0
        results.forEach { (id, isInstalled) ->
            settings.setAgentActive(id, isInstalled)
            if (isInstalled) enabledCount++
        }

        return Pair(enabledCount, results.size)
    }
}
