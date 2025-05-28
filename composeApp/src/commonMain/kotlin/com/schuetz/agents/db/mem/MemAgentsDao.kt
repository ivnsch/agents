package com.schuetz.agents.db.mem

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MemAgentsDao(seeder: DataSeeder) : AgentsDao {
    private val agents = MutableStateFlow<List<AgentData>>(emptyList())

    init {
        CoroutineScope(Dispatchers.Default).launch {
            val seed = seeder.seed()
            agents.value = listOf(seed.agents.me, seed.agents.dummy)
        }
    }

    override val all: Flow<List<AgentData>> = agents

    override suspend fun insert(agent: AgentInput): AgentData {
        val data = AgentData(
            id = agents.value.size.toLong(),
            name = agent.name,
            isMe = agent.isMe,
            avatarUrl = agent.avatarUrl
        )
        agents.update { it + data }
        return AgentData(data.id, data.name, data.isMe, data.avatarUrl)
    }

    override suspend fun count(): Long = agents.value.size.toLong()

    override suspend fun getAll(): List<AgentData> = agents.value
}
