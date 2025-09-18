package com.llmbrains.actions

import com.intellij.openapi.project.Project
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class LlmBrainsCliActionTest {

    @Test
    fun `runWithProject delegates to runner`() {
        val project = mockk<Project>(relaxed = true)
        val recordingRunner = RecordingRunner()
        var capturedProject: Project? = null

        val action = LlmBrainsCliAction(
            text = "Claude",
            description = "desc",
            commandProvider = { LlmBrainsTerminalCommand("🫴 Claude", "claude") },
            runnerFactory = { projectArg ->
                capturedProject = projectArg
                recordingRunner
            },
        )

        action.runWithProject(project)

        assertSame(project, capturedProject)
        assertEquals("claude", recordingRunner.lastCommand)
        assertEquals("🫴 Claude", recordingRunner.lastTab)
    }

    private class RecordingRunner : LlmBrainsTerminalRunner {
        var lastCommand: String? = null
        var lastTab: String? = null

        override fun run(command: String, tabTitle: String) {
            lastCommand = command
            lastTab = tabTitle
        }
    }
}
