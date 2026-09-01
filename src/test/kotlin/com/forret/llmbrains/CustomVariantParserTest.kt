package com.forret.llmbrains

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CustomVariantParserTest {
    private val ids = setOf("claude", "codex")

    @Test
    fun `should parse two-field line as claude variant`() {
        val entry = CustomVariantParser.parse("Plan mode | --permission-mode plan", ids).single()

        assertEquals("claude", entry.agentId)
        assertEquals("Plan mode", entry.label)
        assertEquals("--permission-mode plan", entry.extraArgs)
        assertEquals(DEFAULT_VARIANT_EMOJI, entry.emoji)
    }

    @Test
    fun `should parse three-field line with known agent id`() {
        val entry = CustomVariantParser.parse("codex | GPT-5 | --model gpt-5", ids).single()

        assertEquals("codex", entry.agentId)
        assertEquals("GPT-5", entry.label)
        assertEquals("--model gpt-5", entry.extraArgs)
    }

    @Test
    fun `should treat unknown first field as label`() {
        val entry = CustomVariantParser.parse("gemini-x | Foo | --bar", ids).single()

        assertEquals("claude", entry.agentId)
        assertEquals("gemini-x", entry.label)
        assertEquals("Foo|--bar", entry.extraArgs)
    }

    @Test
    fun `should keep pipes in the last field`() {
        val entry = CustomVariantParser.parse("Sys | --append-system-prompt a|b", ids).single()

        assertEquals("--append-system-prompt a|b", entry.extraArgs)
    }

    @Test
    fun `should skip blank comment and single-field lines`() {
        assertTrue(CustomVariantParser.parse("\n# comment\nonlylabel\n   \n", ids).isEmpty())
    }

    @Test
    fun `should skip lines with a blank label`() {
        assertTrue(CustomVariantParser.parse(" | --model opus\ncodex |  | --x", ids).isEmpty())
    }

    @Test
    fun `should be case-insensitive for agent id`() {
        assertEquals("codex", CustomVariantParser.parse("CODEX | X | --y", ids).single().agentId)
    }
}
