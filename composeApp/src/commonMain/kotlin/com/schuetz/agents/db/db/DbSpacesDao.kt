package com.schuetz.agents.db.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.SpacesDao
import com.schuetz.agents.domain.AgentConnectionData.None.toConnectionData
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.domain.SpaceInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class DbSpacesDao(
    database: MyDatabase,
    dispatcher: CoroutineDispatcher,
    private val agentsDao: AgentsDao
) : SpacesDao {
    private val spaceQueries = database.spaceQueries

    override val all: Flow<List<SpaceData>> = database.spaceQueries
        .selectAll()
        .asFlow()
        .mapToList(dispatcher)
        .map { spaces ->
            spaces.map {
                SpaceData(
                    it.id,
                    it.name,
                    AgentData(
                        it.agent_id,
                        it.agent_name,
                        it.agent_is_me,
                        it.agent_avatar_url,
                        toConnectionData(it.agent_provider, it.agent_api_key)
                    )
                )
            }
        }
        .flowOn(dispatcher)

    override suspend fun insert(space: SpaceInput): SpaceData {
        // TODO in a transaction
        val agent = agentsDao.insert(space.agent)
        spaceQueries.insert(space.name, agent.id)
        val spaceId = spaceQueries.lastInsertId().executeAsOne()
        return SpaceData(spaceId, space.name, agent)
    }
}
