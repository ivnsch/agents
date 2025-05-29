package com.schuetz.agents

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.domain.AgentConnectionData
import com.schuetz.agents.domain.AgentInput

interface InitAppService {
    suspend fun init()
}

class InitAppServiceImpl(
    private val agentsDao: AgentsDao,
    private val avatarUrlGenerator: AvatarUrlGenerator
) : InitAppService {
    override suspend fun init() {
        insertMyAgentIfNotExists()
    }

    // Inserts the agent representing "me" if not in db yet (running for the first time)
    private suspend fun insertMyAgentIfNotExists() {
        if (agentsDao.count() == 0L) {
            agentsDao.insert(
                AgentInput(
                    name = "me",
                    description = null,
                    isMe = true,
                    avatarUrl = avatarUrlGenerator.generateRandomAvatarUrl(),
                    connectionData = AgentConnectionData.None
                )
            )
        }
    }
}
