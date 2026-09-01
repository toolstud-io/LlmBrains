package com.forret.llmbrains

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

/**
 * One row of the "Custom invocations" table, as persisted in LlmBrainsAgentSettings.xml.
 * Mutable bean with defaults so the IntelliJ XML serializer can round-trip it.
 */
data class CustomVariantEntry(
    var agentId: String = CustomVariantParser.DEFAULT_AGENT_ID,
    var label: String = "",
    var extraArgs: String = "",
    var emoji: String = DEFAULT_VARIANT_EMOJI,
) {
    val isValid: Boolean get() = agentId.isNotBlank() && label.isNotBlank()

    fun toVariant(index: Int): AgentVariant = AgentVariant(
        id = "custom-variant:$agentId:$index",
        agentId = agentId,
        label = label.trim(),
        extraArgs = extraArgs.trim(),
        emoji = emoji,
    )
}

@Service(Service.Level.APP)
@State(name = "LlmBrainsAgentSettings", storages = [Storage("LlmBrainsAgentSettings.xml")])
class AgentSettingsState : PersistentStateComponent<AgentSettingsState.State> {
    data class State(
        var inactiveAgentIds: MutableList<String> = mutableListOf(),
        var customAgentEnabled: Boolean = false,
        var customAgentName: String = "",
        var customAgentCommand: String = "",
        var customAgentUrl: String = "",
        var customVariants: MutableList<CustomVariantEntry> = mutableListOf(),
        /** Legacy 0.6.x free-text format; migrated into [customVariants] on load. */
        var customVariantLines: String = "",
    )

    private var state: State = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
        migrateLegacyVariantLines()
    }

    private fun migrateLegacyVariantLines() {
        if (state.customVariantLines.isBlank()) return
        state.customVariants.addAll(CustomVariantParser.parse(state.customVariantLines))
        state.customVariantLines = ""
    }

    fun isAgentActive(id: String): Boolean = id !in state.inactiveAgentIds

    fun setAgentActive(id: String, active: Boolean) {
        if (active) {
            state.inactiveAgentIds.remove(id)
        } else if (id !in state.inactiveAgentIds) {
            state.inactiveAgentIds.add(id)
        }
    }

    fun enableAllAgents() {
        state.inactiveAgentIds.clear()
    }

    fun activeAgents(): List<CodingAgent> = CodingAgents.all.filter { isAgentActive(it.id) }

    fun customVariants(): List<AgentVariant> =
        state.customVariants.mapIndexedNotNull { index, entry -> if (entry.isValid) entry.toVariant(index) else null }

    fun variantsFor(agentId: String): List<AgentVariant> = customVariants().filter { it.agentId == agentId }

    fun getCustomAgent(): CodingAgent? {
        if (!state.customAgentEnabled || state.customAgentName.isBlank() || state.customAgentCommand.isBlank()) {
            return null
        }
        return CodingAgent(
            id = "custom",
            name = state.customAgentName.trim(),
            command = state.customAgentCommand.trim(),
            versionArgs = "--version",
            installHint = "",
            updateHint = "",
            url = state.customAgentUrl.trim().ifBlank { "https://example.com" },
        )
    }

    companion object {
        fun getInstance(): AgentSettingsState = service()
    }
}
