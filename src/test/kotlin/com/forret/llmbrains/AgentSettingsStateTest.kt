package com.forret.llmbrains

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AgentSettingsStateTest {
    @Test
    fun `should list custom invocations per agent with stable ids`() {
        val settings = AgentSettingsState()
        settings.loadState(
            AgentSettingsState.State(
                customVariants = mutableListOf(
                    CustomVariantEntry("claude", "Claude Fable", "--model fable", "🔴"),
                    CustomVariantEntry("codex", "Codex yolo", "--yolo"),
                    CustomVariantEntry("claude", "Plan", "--permission-mode plan"),
                ),
            ),
        )

        val claude = settings.variantsFor("claude")
        assertEquals(listOf("custom-variant:claude:0", "custom-variant:claude:2"), claude.map { it.id })
        assertEquals("🔴 Claude Fable", claude[0].dropdownLabel)
        assertEquals(listOf("Codex yolo"), settings.variantsFor("codex").map { it.label })
        assertTrue(settings.variantsFor("gemini").isEmpty())
    }

    @Test
    fun `should skip rows without a label`() {
        val settings = AgentSettingsState()
        settings.loadState(AgentSettingsState.State(customVariants = mutableListOf(CustomVariantEntry("claude", "  ", "--x"))))

        assertTrue(settings.customVariants().isEmpty())
    }

    @Test
    fun `should migrate legacy text lines into table entries`() {
        val settings = AgentSettingsState()
        settings.loadState(
            AgentSettingsState.State(
                customVariants = mutableListOf(CustomVariantEntry("claude", "Existing", "--a")),
                customVariantLines = "Plan mode | --permission-mode plan\ncodex | GPT-5 | --model gpt-5",
            ),
        )

        val state = settings.getState()
        assertEquals("", state.customVariantLines)
        assertEquals(listOf("Existing", "Plan mode", "GPT-5"), state.customVariants.map { it.label })
        assertEquals("codex", state.customVariants[2].agentId)
    }
}
