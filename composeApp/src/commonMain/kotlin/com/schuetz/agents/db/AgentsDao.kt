package com.schuetz.agents.db

import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow

interface AgentsDao {
    val all: Flow<List<AgentData>>

    suspend fun insert(agent: AgentInput): AgentData
    suspend fun count(): Long

    // TODO remove
    suspend fun getAll(): List<AgentData>
}

