package com.schuetz.agents.db.mem

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import comschuetzagents.data.Agent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MemAgentsDao : AgentsDao {
    private val agents = MutableStateFlow<List<AgentData>>(emptyList())

    override fun all(): Flow<List<AgentData>> = agents

    override fun insert(agent: AgentInput): Agent {
        val data = AgentData(id = agents.value.size.toLong(), name = agent.name, isMe = agent.isMe)
        agents.update { it + data }
        // TODO don't return Agent, which is a db type
        return Agent(data.id, data.name, data.isMe)
    }

    override fun count(): Long = agents.value.size.toLong()

    override fun getAll(): List<AgentData> = agents.value
}
