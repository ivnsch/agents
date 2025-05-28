package com.schuetz.agents.db.mem

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.SpacesDao
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.domain.SpaceInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MemSpacesDao(
    private val agentsDao: AgentsDao
) : SpacesDao {
    override val all = MutableStateFlow<List<SpaceData>>(emptyList())

    override suspend fun insert(space: SpaceInput): SpaceData {
        val agent = agentsDao.insert(space.agent)
        val data = SpaceData(
            id = all.value.size.toLong(),
            name = space.name,
            agent = agent
        )
        all.update { it + data }
        return SpaceData(data.id, data.name, data.agent)
    }
}
