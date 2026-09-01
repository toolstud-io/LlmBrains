package com.forret.llmbrains

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AgentVariantTest {
    private val claude = CodingAgents.findById("claude")!!

    @Test
    fun `should build command from parent agent`() {
        assertEquals("claude --model fable", AgentVariant("x", "claude", "Fable", "--model fable").commandFor(claude))
        assertEquals("claude", AgentVariant("x", "claude", "Plain", "").commandFor(claude))
    }

    @Test
    fun `should prefix label with emoji in dropdown and tab title`() {
        val variant = AgentVariant("x", "claude", "Claude Fable", "--model fable", emoji = "🔴")

        assertEquals("🔴 Claude Fable", variant.dropdownLabel)
        assertEquals("🔴 Claude Fable", variant.tabTitle)
    }

    @Test
    fun `should fall back to default emoji when blank`() {
        assertEquals("$DEFAULT_VARIANT_EMOJI X", AgentVariant("x", "claude", "X", "", emoji = "  ").dropdownLabel)
    }

    @Test
    fun `should ship syntax help for claude and codex only`() {
        assertEquals(listOf("claude", "codex"), CodingAgents.withVariantHelp.map { it.id })
        assertTrue(claude.variantHelp.contains("--model <fable|opus|sonnet|haiku"))
        assertTrue(claude.variantHelp.contains("--effort <low|medium|high|xhigh|max>"))
    }
}
