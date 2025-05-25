package com.schuetz.agents.agents

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow

class AgentsRepoImpl(private val agentsDao: AgentsDao) : AgentsRepo {
    override val agents: Flow<List<AgentData>> = agentsDao.all

    override fun addAgent(agent: AgentInput) {
        agentsDao.insert(agent)
    }
}
