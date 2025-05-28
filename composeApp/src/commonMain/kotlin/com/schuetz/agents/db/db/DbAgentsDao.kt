package com.schuetz.agents.db.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.multipleMeAgentsError
import com.schuetz.agents.db.noMeAgentError
import com.schuetz.agents.domain.AgentConnectionData
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class DbAgentsDao(
    database: MyDatabase,
    dispatcher: CoroutineDispatcher
) : AgentsDao {
    private val agentQueries = database.agentQueries

    override val all: Flow<List<AgentData>> =
        agentQueries
            .selectAll()
            .asFlow()
            .mapToList(dispatcher)
            .map { agents ->
                agents.map {
                    AgentData(
                        id = it.id,
                        name = it.name,
                        isMe = it.is_me,
                        avatarUrl = it.avatar_url,
                        connectionData = toConnectionData(it.provider, it.api_key)
                    )
                }
            }
            .flowOn(dispatcher)

    override val me: Flow<AgentData> =
        agentQueries
            .selectMe()
            .asFlow()
            .mapToList(dispatcher)
            .map { list ->
                when (list.size) {
                    1 -> {
                        list.first().let {
                            AgentData(
                                id = it.id,
                                name = it.name,
                                isMe = it.is_me,
                                avatarUrl = it.avatar_url,
                                connectionData = AgentConnectionData.None
                            )
                        }
                    }

                    0 -> noMeAgentError()
                    else -> multipleMeAgentsError()
                }
            }

    override suspend fun insert(agent: AgentInput): AgentData {
        agentQueries.insert(
            agent.name,
            agent.isMe,
            agent.avatarUrl,
            agent.connectionData.providerStr(),
            agent.connectionData.apiKey()
        )
        val id = agentQueries.lastInsertId().executeAsOne()
        return AgentData(id, agent.name, agent.isMe, agent.avatarUrl, agent.connectionData)
    }

    override suspend fun count(): Long =
        agentQueries.countAgents().executeAsOne()

    override suspend fun getAll(): List<AgentData> {
        return agentQueries.selectAll().executeAsList().map {
            AgentData(
                id = it.id,
                name = it.name,
                isMe = it.is_me,
                avatarUrl = it.avatar_url,
                connectionData = toConnectionData(it.provider, it.api_key)
            )
        }
    }
}

private fun toConnectionData(provider: String, apiKey: String?): AgentConnectionData {
    return when (provider) {
        "huggingface" -> apiKey?.let { AgentConnectionData.HuggingFace(it) }
            ?: throw IllegalArgumentException("Missing API key for $provider")

        else -> throw IllegalArgumentException("Unknown provider: $provider")
    }
}
