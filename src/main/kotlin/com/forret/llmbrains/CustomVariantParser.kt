package com.forret.llmbrains

/**
 * Parses the "custom variants" text area into [AgentVariant]s. One variant per line:
 *
 * ```
 * # comment                       -> ignored
 * Label | extra args              -> variant of the default agent (claude)
 * agent-id | Label | extra args   -> variant of the given agent (must be a known id)
 * ```
 *
 * The last field keeps any embedded '|' characters. Malformed lines are skipped silently.
 */
object CustomVariantParser {
    const val DEFAULT_AGENT_ID = "claude"

    fun parse(text: String, knownAgentIds: Set<String> = CodingAgents.ids): List<AgentVariant> =
        text.lines().mapIndexedNotNull { index, rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith("#")) return@mapIndexedNotNull null
            val parts = line.split("|").map { it.trim() }
            if (parts.size < 2) return@mapIndexedNotNull null
            val first = parts[0].lowercase()
            val (agentId, label, extraArgs) = if (first in knownAgentIds && parts.size >= 3) {
                Triple(first, parts[1], parts.drop(2).joinToString("|"))
            } else {
                Triple(DEFAULT_AGENT_ID, parts[0], parts.drop(1).joinToString("|"))
            }
            if (label.isBlank()) return@mapIndexedNotNull null
            AgentVariant(
                id = "custom-variant:$agentId:$index",
                agentId = agentId,
                label = label,
                extraArgs = extraArgs,
            )
        }
}
