package com.forret.llmbrains

private const val HAND_EMOJI = "🫴"

data class CodingAgent(
    val id: String,
    val name: String,
    val command: String,
    val versionArgs: String = "--version",
    val installHint: String,
    val updateHint: String,
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
            updateHint = "npm update --quiet --no-fund -g @anthropic-ai/claude-code",
        ),
        CodingAgent(
            id = "codex",
            name = "Codex CLI",
            command = "codex",
            installHint = "npm install -g @openai/codex",
            updateHint = "npm update --quiet --no-fund -g @openai/codex",
        ),
        CodingAgent(
            id = "gemini",
            name = "Gemini CLI",
            command = "gemini",
            installHint = "npm install -g @google/gemini-cli",
            updateHint = "npm update --quiet --no-fund -g @google/gemini-cli",
        ),
        CodingAgent(
            id = "grok",
            name = "Grok CLI",
            command = "grok",
            installHint = "npm install -g @vibe-kit/grok-cli",
            updateHint = "npm update --quiet --no-fund -g @vibe-kit/grok-cli",
        ),
        CodingAgent(
            id = "qodo",
            name = "Qodo Command",
            command = "qodo",
            installHint = "npm install -g @qodo/command",
            updateHint = "npm update --quiet --no-fund -g @qodo/command",
        ),
        CodingAgent(
            id = "opencode",
            name = "OpenCode",
            command = "opencode",
            installHint = "npm install -g opencode-ai",
            updateHint = "npm update --quiet --no-fund -g opencode-ai",
        ),
    )

    fun findById(id: String): CodingAgent? = all.firstOrNull { it.id == id }
}
