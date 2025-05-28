package com.schuetz.agents.spaces

import com.schuetz.agents.db.SpacesDao
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.domain.SpaceInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SpacesRepoImpl(
    private val spacesDao: SpacesDao,
    private val dispatcher: CoroutineDispatcher
) : SpacesRepo {
    override val spaces: Flow<List<SpaceData>> = spacesDao.all

    override suspend fun addSpace(space: SpaceInput) {
        withContext(dispatcher) {
            spacesDao.insert(space)
        }
    }
}
