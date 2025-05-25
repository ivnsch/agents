package com.schuetz.agents.db.mem

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MemAgentsDao(seeder: DataSeeder) : AgentsDao {
    private val seed = seeder.seed()
    private val agents = MutableStateFlow(
        listOf(seed.agents.me, seed.agents.dummy)
    )

    override val all: Flow<List<AgentData>> = agents

    override fun insert(agent: AgentInput): AgentData {
        val data = AgentData(id = agents.value.size.toLong(), name = agent.name, isMe = agent.isMe)
        agents.update { it + data }
        return AgentData(data.id, data.name, data.isMe)
    }

    override fun count(): Long = agents.value.size.toLong()

    override fun getAll(): List<AgentData> = agents.value
}
