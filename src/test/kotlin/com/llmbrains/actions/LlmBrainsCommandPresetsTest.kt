package com.llmbrains.actions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LlmBrainsCommandPresetsTest {

    @Test
    fun `claude command exposes expected metadata`() {
        val command = LlmBrainsCommandPresets.claude()

        assertEquals("🫴 Claude", command.tabTitle)
        assertEquals("claude", command.command)
    }

    @Test
    fun `diagnostic command wraps bash helper`() {
        val command = LlmBrainsCommandPresets.diagnostic()

        assertEquals("🫴 Check", command.tabTitle)
        assertTrue(command.command.startsWith("bash -lc '"))
        assertTrue(command.command.contains("claude --version"))
        assertTrue(command.command.contains("codex --version"))
        assertTrue(command.command.contains("gemini --version"))
        assertTrue(command.command.endsWith("'"))
    }
}
