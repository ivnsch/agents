package com.schuetz.agents.agents

import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AgentsRepo {
    val agents: Flow<List<AgentData>>
    val otherAgents: Flow<List<AgentData>>
        get() = agents.map { agents -> agents.filter { !it.isMe } }

    fun addAgent(agent: AgentInput)
}
