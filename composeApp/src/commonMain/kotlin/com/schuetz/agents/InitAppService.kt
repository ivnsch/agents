package com.schuetz.agents

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.domain.AgentInput
import com.schuetz.agents.huggingface.HuggingFaceTokenStore

interface InitAppService {
    suspend fun init()
}

class InitAppServiceImpl(
    private val huggingFaceTokenStore: HuggingFaceTokenStore,
    private val agentsDao: AgentsDao,
    private val avatarUrlGenerator: AvatarUrlGenerator
) : InitAppService {
    override suspend fun init() {
        insertMyAgentIfNotExists()
        huggingFaceTokenStore.initialize()
    }

    // Inserts the agent representing "me" if not in db yet (running for the first time)
    private suspend fun insertMyAgentIfNotExists() {
        if (agentsDao.count() == 0L) {
            agentsDao.insert(
                AgentInput(
                    name = "me",
                    isMe = true,
                    avatarUrl = avatarUrlGenerator.generateRandomAvatarUrl()
                )
            )
        }
    }
}
