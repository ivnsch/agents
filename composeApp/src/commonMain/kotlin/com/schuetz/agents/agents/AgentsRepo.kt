package com.schuetz.agents.agents

import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow

interface AgentsRepo {
    val agents: Flow<List<AgentData>>
    val me: Flow<AgentData>

    suspend fun addAgent(agent: AgentInput)
}
