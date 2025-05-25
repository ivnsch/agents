package com.schuetz.agents.db

import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow

interface AgentsDao {
    val all: Flow<List<AgentData>>

    fun insert(agent: AgentInput): AgentData
    fun count(): Long

    // TODO remove
    fun getAll(): List<AgentData>
}

