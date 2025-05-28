package com.schuetz.agents.db

import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.domain.SpaceInput
import kotlinx.coroutines.flow.Flow

interface SpacesDao {
    val all: Flow<List<SpaceData>>

    suspend fun insert(space: SpaceInput): SpaceData
}

