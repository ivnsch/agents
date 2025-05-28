package com.schuetz.agents.db.mem

import com.schuetz.agents.AvatarUrlGenerator
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.db.SeededData
import com.schuetz.agents.domain.AgentData

class MemDataSeeder(
    avatarUrlGenerator: AvatarUrlGenerator
) : DataSeeder {
    val data = SeededData(
        SeededData.Agents(
            me = AgentData(
                id = 1,
                name = "me",
                isMe = true,
                avatarUrl = avatarUrlGenerator.generateRandomAvatarUrl()
            ),
            dummy = AgentData(
                id = 2,
                name = "dummy",
                isMe = false,
                avatarUrl = avatarUrlGenerator.generateRandomAvatarUrl()
            )
        )
    )

    override suspend fun seed(): SeededData = data
}
