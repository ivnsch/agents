package com.schuetz.agents.db

import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import comschuetzagents.data.Agent
import kotlinx.coroutines.flow.Flow

interface AgentsDao {
    fun all(): Flow<List<AgentData>>
    fun insert(agent: AgentInput): Agent
    fun count(): Long

    // TODO remove
    fun getAll(): List<AgentData>
}

