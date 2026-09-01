package com.forret.llmbrains

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CustomVariantParserTest {
    private val ids = setOf("claude", "codex")

    @Test
    fun `should parse two-field line as claude variant`() {
        val variant = CustomVariantParser.parse("Plan mode | --permission-mode plan", ids).single()

        assertEquals("claude", variant.agentId)
        assertEquals("Plan mode", variant.label)
        assertEquals("--permission-mode plan", variant.extraArgs)
    }

    @Test
    fun `should parse three-field line with known agent id`() {
        val variant = CustomVariantParser.parse("codex | GPT-5 | --model gpt-5", ids).single()

        assertEquals("codex", variant.agentId)
        assertEquals("GPT-5", variant.label)
        assertEquals("--model gpt-5", variant.extraArgs)
    }

    @Test
    fun `should treat unknown first field as label`() {
        val variant = CustomVariantParser.parse("gemini-x | Foo | --bar", ids).single()

        assertEquals("claude", variant.agentId)
        assertEquals("gemini-x", variant.label)
        assertEquals("Foo|--bar", variant.extraArgs)
    }

    @Test
    fun `should keep pipes in the last field`() {
        val variant = CustomVariantParser.parse("Sys | --append-system-prompt a|b", ids).single()

        assertEquals("--append-system-prompt a|b", variant.extraArgs)
    }

    @Test
    fun `should skip blank comment and single-field lines`() {
        val variants = CustomVariantParser.parse("\n# comment\nonlylabel\n   \n", ids)

        assertTrue(variants.isEmpty())
    }

    @Test
    fun `should skip lines with a blank label`() {
        val variants = CustomVariantParser.parse(" | --model opus\ncodex |  | --x", ids)

        assertTrue(variants.isEmpty())
    }

    @Test
    fun `should be case-insensitive for agent id`() {
        val variant = CustomVariantParser.parse("CODEX | X | --y", ids).single()

        assertEquals("codex", variant.agentId)
    }

    @Test
    fun `should use original line index in id`() {
        val variant = CustomVariantParser.parse("\n\nA | --a", ids).single()

        assertEquals("custom-variant:claude:2", variant.id)
    }

    @Test
    fun `should build command from parent agent`() {
        val agent = CodingAgents.findById("claude")!!

        assertEquals("claude --model fable", AgentVariant("x", "claude", "Fable", "--model fable").commandFor(agent))
        assertEquals("claude", AgentVariant("x", "claude", "Plain", "").commandFor(agent))
    }

    @Test
    fun `should only enable presets by default that are not dangerous`() {
        val presets = CodingAgents.presetVariantsFor("claude")

        assertEquals(4, presets.size)
        assertEquals(listOf("claude-opus-skip-permissions"), presets.filterNot { it.defaultEnabled }.map { it.id })
    }
}
