package com.forret.llmbrains

/**
 * Parses the legacy (0.6.x) "custom variants" text area into [CustomVariantEntry]s. One variant per line:
 *
 * ```
 * # comment                       -> ignored
 * Label | extra args              -> variant of the default agent (claude)
 * agent-id | Label | extra args   -> variant of the given agent (must be a known id)
 * ```
 *
 * The last field keeps any embedded '|' characters. Malformed lines are skipped silently.
 * Only used to migrate old settings into the structured table; new entries are edited in the settings UI.
 */
object CustomVariantParser {
    const val DEFAULT_AGENT_ID = "claude"

    fun parse(text: String, knownAgentIds: Set<String> = CodingAgents.ids): List<CustomVariantEntry> =
        text.lines().mapNotNull { rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith("#")) return@mapNotNull null
            val parts = line.split("|").map { it.trim() }
            if (parts.size < 2) return@mapNotNull null
            val first = parts[0].lowercase()
            val (agentId, label, extraArgs) = if (first in knownAgentIds && parts.size >= 3) {
                Triple(first, parts[1], parts.drop(2).joinToString("|"))
            } else {
                Triple(DEFAULT_AGENT_ID, parts[0], parts.drop(1).joinToString("|"))
            }
            if (label.isBlank()) return@mapNotNull null
            CustomVariantEntry(agentId = agentId, label = label, extraArgs = extraArgs)
        }
}
