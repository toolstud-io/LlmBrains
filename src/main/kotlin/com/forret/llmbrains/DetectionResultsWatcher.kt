package com.forret.llmbrains

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object DetectionResultsWatcher {
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var watchTask: ScheduledFuture<*>? = null

    private const val POLL_INTERVAL_MS = 500L
    private const val MAX_WAIT_MS = 60_000L

    fun watchForResults(
        project: Project,
        resultsFilePath: Path,
        onComplete: (Map<String, Boolean>) -> Unit,
    ) {
        val startTime = System.currentTimeMillis()

        watchTask?.cancel(false)
        watchTask = executor.scheduleAtFixedRate({
            try {
                if (Files.exists(resultsFilePath) && Files.size(resultsFilePath) > 0) {
                    val content = Files.readString(resultsFilePath)
                    val results = parseResults(content)

                    if (results.isNotEmpty()) {
                        watchTask?.cancel(false)

                        // Delete the temp file
                        Files.deleteIfExists(resultsFilePath)

                        // Invoke callback on EDT
                        ApplicationManager.getApplication().invokeLater {
                            onComplete(results)
                        }
                    }
                } else if (System.currentTimeMillis() - startTime > MAX_WAIT_MS) {
                    watchTask?.cancel(false)
                    ApplicationManager.getApplication().invokeLater {
                        showNotification(project, "Agent detection timed out", NotificationType.WARNING)
                    }
                }
            } catch (e: Exception) {
                // Continue polling on error
            }
        }, POLL_INTERVAL_MS, POLL_INTERVAL_MS, TimeUnit.MILLISECONDS)
    }

    private fun parseResults(content: String): Map<String, Boolean> {
        val results = mutableMapOf<String, Boolean>()
        content.lines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.contains("=")) {
                val parts = trimmed.split("=", limit = 2)
                if (parts.size == 2) {
                    val id = parts[0].trim()
                    val value = parts[1].trim()
                    if (id.isNotEmpty()) {
                        results[id] = value == "1"
                    }
                }
            }
        }
        return results
    }

    fun applyResults(results: Map<String, Boolean>): Pair<Int, Int> {
        val settings = AgentSettingsState.getInstance()
        settings.enableAllAgents()

        var enabledCount = 0
        results.forEach { (id, isInstalled) ->
            settings.setAgentActive(id, isInstalled)
            if (isInstalled) enabledCount++
        }

        return Pair(enabledCount, results.size)
    }

    fun showNotification(project: Project?, message: String, type: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("LLM Brains")
            .createNotification(message, type)
            .notify(project)
    }
}
