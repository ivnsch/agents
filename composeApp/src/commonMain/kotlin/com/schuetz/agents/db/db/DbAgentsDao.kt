package com.schuetz.agents.db.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DbAgentsDao(database: MyDatabase) : AgentsDao {
    private val agentQueries = database.agentQueries

    override val all: Flow<List<AgentData>> =
        agentQueries
            .selectAll()
            .asFlow()
            .mapToList(dispatcher)
            .map { agents ->
                agents.map {
                    AgentData(id = it.id, name = it.name, isMe = it.is_me)
                }
            }

    override fun insert(agent: AgentInput): AgentData {
        agentQueries.insert(agent.name, agent.isMe)
        val id = agentQueries.lastInsertId().executeAsOne()
        return AgentData(id, agent.name, agent.isMe)
    }

    override fun count(): Long =
        agentQueries.countAgents().executeAsOne()

    override fun getAll(): List<AgentData> {
        return agentQueries.selectAll().executeAsList().map {
            AgentData(id = it.id, name = it.name, isMe = it.is_me)
        }
    }
}
