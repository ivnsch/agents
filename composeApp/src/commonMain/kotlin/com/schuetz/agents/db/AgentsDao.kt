package com.schuetz.agents.db

import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow

interface AgentsDao {
    val all: Flow<List<AgentData>>
    val me: Flow<AgentData>

    suspend fun insert(agent: AgentInput): AgentData
    suspend fun count(): Long

    // TODO remove
    suspend fun getAll(): List<AgentData>
}

fun noMeAgentError(): Nothing =
    error("Invalid state: no \"me\" agent. A \"me\" agent should be created on app initialization")

fun multipleMeAgentsError(): Nothing =
    error("Invalid state: multiple \"me\" agents")
