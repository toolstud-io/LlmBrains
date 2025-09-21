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

    fun activeAgents(): List<CodingAgent> = CodingAgents.all.filter { isAgentActive(it.id) }

    companion object {
        fun getInstance(): AgentSettingsState = service()
    }
}
