package com.schuetz.agents.db.mem

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.multipleMeAgentsError
import com.schuetz.agents.db.noMeAgentError
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MemAgentsDao : AgentsDao {
    private val agents = MutableStateFlow<List<AgentData>>(emptyList())

    override val me: Flow<AgentData> =
        agents
            .map { agents -> agents.filter { it.isMe } }
            .map { list ->
                when (list.size) {
                    1 -> list.first()
                    0 -> noMeAgentError()
                    else -> multipleMeAgentsError()
                }
            }

    override val all: Flow<List<AgentData>> = agents

    override suspend fun insert(agent: AgentInput): AgentData {
        val data = AgentData(
            id = agents.value.size.toLong(),
            name = agent.name,
            isMe = agent.isMe,
            avatarUrl = agent.avatarUrl,
            connectionData = agent.connectionData
        )
        agents.update { it + data }
        return AgentData(data.id, data.name, data.isMe, data.avatarUrl, data.connectionData)
    }

    override suspend fun count(): Long = agents.value.size.toLong()

    override suspend fun getAll(): List<AgentData> = agents.value
}
