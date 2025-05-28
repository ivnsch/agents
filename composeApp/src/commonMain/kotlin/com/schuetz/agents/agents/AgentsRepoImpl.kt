package com.schuetz.agents.agents

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AgentsRepoImpl(
    private val agentsDao: AgentsDao,
    private val dispatcher: CoroutineDispatcher
) : AgentsRepo {
    override val agents: Flow<List<AgentData>> = agentsDao.all
    override val me: Flow<AgentData> = agentsDao.me

    override suspend fun addAgent(agent: AgentInput) {
        withContext(dispatcher) {
            agentsDao.insert(agent)
        }
    }
}
