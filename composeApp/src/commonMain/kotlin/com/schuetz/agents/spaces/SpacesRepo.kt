package com.schuetz.agents.spaces

import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.domain.SpaceInput
import kotlinx.coroutines.flow.Flow

interface SpacesRepo {
    val spaces: Flow<List<SpaceData>>

    suspend fun addSpace(space: SpaceInput)
}
