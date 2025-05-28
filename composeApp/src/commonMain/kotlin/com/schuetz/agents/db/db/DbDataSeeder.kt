package com.schuetz.agents.db.db

import com.schuetz.agents.AvatarUrlGenerator
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.db.SeededData
import com.schuetz.agents.domain.AgentInput

class DbDataSeeder(
    private val agentsDao: AgentsDao,
    private val avatarUrlGenerator: AvatarUrlGenerator
) : DataSeeder {
    // TODO remove
    override suspend fun seed(): SeededData = if (agentsDao.count() == 0L) {
        SeededData(
            agents = SeededData.Agents(
                me = agentsDao.insert(
                    AgentInput(
                        name = "me",
                        isMe = true,
                        avatarUrl = avatarUrlGenerator.generateRandomAvatarUrl()
                    )
                ),
                dummy = agentsDao.insert(
                    AgentInput(
                        name = "dummy",
                        isMe = false,
                        avatarUrlGenerator.generateRandomAvatarUrl()
                    )
                )
            )
        )
    } else {
        val all = agentsDao.getAll()
        SeededData(
            agents = SeededData.Agents(
                me = all.first { it.name == "me" },
                dummy = all.first { it.name == "dummy" }
            )
        )
    }
}
