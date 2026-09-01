package com.forret.llmbrains

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.components.Service

@Service(Service.Level.APP)
@State(name = "LlmBrainsAgentSettings", storages = [Storage("LlmBrainsAgentSettings.xml")])
class AgentSettingsState : PersistentStateComponent<AgentSettingsState.State> {
    data class State(
        var inactiveAgentIds: MutableList<String> = mutableListOf(),
        var customAgentEnabled: Boolean = false,
        var customAgentName: String = "",
        var customAgentCommand: String = "",
        var customAgentUrl: String = "",
        var activeVariantIds: MutableList<String> = mutableListOf(),
        var inactiveVariantIds: MutableList<String> = mutableListOf(),
        var customVariantLines: String = "",
    )

    private var state: State = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
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

    fun isVariantActive(variant: AgentVariant): Boolean = when {
        variant.id in state.activeVariantIds -> true
        variant.id in state.inactiveVariantIds -> false
        else -> variant.defaultEnabled
    }

    /** Only stores an override when the choice differs from the variant's default. */
    fun setVariantActive(variant: AgentVariant, active: Boolean) {
        state.activeVariantIds.remove(variant.id)
        state.inactiveVariantIds.remove(variant.id)
        if (active != variant.defaultEnabled) {
            if (active) state.activeVariantIds.add(variant.id) else state.inactiveVariantIds.add(variant.id)
        }
    }

    fun customVariants(): List<AgentVariant> = CustomVariantParser.parse(state.customVariantLines)

    fun activeVariantsFor(agentId: String): List<AgentVariant> =
        CodingAgents.presetVariantsFor(agentId).filter { isVariantActive(it) } +
            customVariants().filter { it.agentId == agentId }

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
