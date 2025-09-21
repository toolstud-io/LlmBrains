package com.forret.llmbrains

private const val HAND_EMOJI = "🫴"

data class CodingAgent(
    val id: String,
    val name: String,
    val command: String,
    val versionArgs: String = "--version",
    val installHint: String,
) {
    val dropdownLabel: String get() = "$HAND_EMOJI $name"
}

object CodingAgents {
    val all: List<CodingAgent> = listOf(
        CodingAgent(
            id = "claude",
            name = "Claude Code",
            command = "claude",
            installHint = "npm install -g @anthropic-ai/claude-code",
        ),
        CodingAgent(
            id = "codex",
            name = "Codex CLI",
            command = "codex",
            installHint = "npm install -g @openai/codex",
        ),
        CodingAgent(
            id = "gemini",
            name = "Gemini CLI",
            command = "gemini",
            installHint = "npm install -g @google/gemini-cli",
        ),
        CodingAgent(
            id = "qodo",
            name = "Qodo Command",
            command = "qodo",
            installHint = "npm install -g @qodo/command",
        ),
    )

    fun findById(id: String): CodingAgent? = all.firstOrNull { it.id == id }
}
