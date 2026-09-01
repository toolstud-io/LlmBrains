package com.forret.llmbrains

internal const val HAND_EMOJI = "🫴"

data class CodingAgent(
    val id: String,
    val name: String,
    val command: String,
    val versionArgs: String = "--version",
    val installHint: String,
    val updateHint: String,
    val url: String,
    /** One-line CLI syntax hint shown in the settings panel next to the custom invocations table. */
    val variantHelp: String = "",
) {
    val dropdownLabel: String get() = "$HAND_EMOJI $name"
}

/**
 * A custom invocation of an existing agent: same binary, extra command-line parameters,
 * with its own emoji prefix so it can be told apart in the dropdown and the terminal tab title.
 */
data class AgentVariant(
    val id: String,
    val agentId: String,
    val label: String,
    val extraArgs: String,
    val emoji: String = DEFAULT_VARIANT_EMOJI,
) {
    private val prefix: String get() = emoji.trim().ifBlank { DEFAULT_VARIANT_EMOJI }

    val dropdownLabel: String get() = "$prefix $label"

    val tabTitle: String get() = dropdownLabel

    fun commandFor(agent: CodingAgent): String = "${agent.command} $extraArgs".trim()
}

const val DEFAULT_VARIANT_EMOJI = HAND_EMOJI

/** Quick picks for the emoji column; any other emoji can be typed. */
val SUGGESTED_VARIANT_EMOJIS: List<String> = listOf("🫴", "🔴", "🟠", "🟡", "🟢", "🔵", "🟣", "🧠", "⚡", "🚀", "🧪", "🛡️")

object CodingAgents {
    val all: List<CodingAgent> = listOf(
        CodingAgent(
            id = "amp",
            name = "Amp CLI",
            command = "amp",
            installHint = "npm install -g @sourcegraph/amp",
            updateHint = "npm update --quiet --no-fund -g @sourcegraph/amp",
            url = "https://ampcode.com/manual#getting-started-command-line-interface",
        ),
        CodingAgent(
            id = "antigravity",
            name = "Antigravity CLI",
            command = "agy",
            installHint = "curl -fsSL https://antigravity.google/cli/install.sh | bash",
            updateHint = "",
            url = "https://antigravity.google/docs/cli/install",
        ),
        CodingAgent(
            id = "claude",
            name = "Claude Code",
            command = "claude",
            installHint = "npm install -g @anthropic-ai/claude-code",
            updateHint = "npm update --quiet --no-fund -g @anthropic-ai/claude-code",
            url = "https://docs.claude.com/en/docs/claude-code/setup",
            variantHelp = "claude --model <fable|opus|sonnet|haiku|full-name> --effort <low|medium|high|xhigh|max> " +
                "--permission-mode <acceptEdits|plan|auto|dontAsk|bypassPermissions> [--dangerously-skip-permissions]",
        ),
        CodingAgent(
            id = "codex",
            name = "Codex CLI",
            command = "codex",
            installHint = "npm install -g @openai/codex",
            updateHint = "npm update --quiet --no-fund -g @openai/codex",
            url = "https://developers.openai.com/codex/cli/",
            variantHelp = "codex --model <name> --sandbox <read-only|workspace-write|danger-full-access> " +
                "--ask-for-approval <untrusted|on-request|never> --profile <name> [--yolo]",
        ),
        CodingAgent(
            id = "copilot",
            name = "Copilot CLI",
            command = "copilot",
            installHint = "npm install -g @github/copilot",
            updateHint = "npm update --quiet --no-fund -g @github/copilot",
            url = "https://github.com/features/copilot/cli",
        ),
        CodingAgent(
            id = "crush",
            name = "Crush CLI",
            command = "crush",
            installHint = "npm install -g @charmland/crush",
            updateHint = "npm update --quiet --no-fund -g @charmland/crush",
            url = "https://github.com/charmbracelet/crush/",
        ),
        CodingAgent(
            id = "droid",
            name = "Droid CLI",
            command = "droid",
            installHint = "curl -fsSL https://app.factory.ai/cli | sh",
            updateHint = "",
            url = "https://factory.ai/product/ide",
        ),
        CodingAgent(
            id = "gemini",
            name = "Gemini CLI (enterprise only)",
            command = "gemini",
            installHint = "npm install -g @google/gemini-cli",
            updateHint = "npm update --quiet --no-fund -g @google/gemini-cli",
            url = "https://github.com/google-gemini/gemini-cli",
        ),
        CodingAgent(
            id = "goose",
            name = "Goose CLI",
            command = "goose",
            installHint = "curl -fsSL https://github.com/block/goose/releases/download/stable/download_cli.sh | bash",
            updateHint = "goose update",
            url = "https://github.com/block/goose",
        ),
        CodingAgent(
            id = "grok",
            name = "Grok CLI",
            command = "grok",
            installHint = "npm install -g @vibe-kit/grok-cli",
            updateHint = "npm update --quiet --no-fund -g @vibe-kit/grok-cli",
            url = "https://github.com/superagent-ai/grok-cli",
        ),
        CodingAgent(
            id = "opencode",
            name = "OpenCode",
            command = "opencode",
            installHint = "npm install -g opencode-ai",
            updateHint = "npm update --quiet --no-fund -g opencode-ai",
            url = "https://opencode.ai/docs",
        ),
        CodingAgent(
            id = "qodo",
            name = "Qodo",
            command = "qodo",
            installHint = "npm install -g @qodo/command",
            updateHint = "npm update --quiet --no-fund -g @qodo/command",
            url = "https://qodo.ai/",
        ),
        CodingAgent(
            id = "qwen",
            name = "Qwen Code",
            command = "qwen",
            installHint = "npm install -g @qwen-code/qwen-code@latest",
            updateHint = "npm update --quiet --no-fund -g @qwen-code/qwen-code",
            url = "https://qwenlm.github.io/qwen-code-docs/en/",
        ),
        CodingAgent(
            id = "vtcode",
            name = "VT Code",
            command = "vtcode",
            installHint = "npm install -g @vinhnx/vtcode --registry=https://npm.pkg.github.com",
            updateHint = "npm update --quiet --no-fund -g @vinhnx/vtcode --registry=https://npm.pkg.github.com",
            url = "https://github.com/vinhnx/vtcode",
        ),
        CodingAgent(
            id = "warp",
            name = "Warp CLI",
            command = "warp",
            installHint = "brew tap warpdotdev/warp && brew update && brew install --cask warp-cli",
            updateHint = "brew upgrade --cask warp-cli",
            url = "https://docs.warp.dev/developers/cli",
        ),
    )

    val ids: Set<String> by lazy { all.map { it.id }.toSet() }

    fun findById(id: String): CodingAgent? = all.firstOrNull { it.id == id }

    /** Agents that document CLI flags worth turning into custom invocations. */
    val withVariantHelp: List<CodingAgent> get() = all.filter { it.variantHelp.isNotBlank() }
}
