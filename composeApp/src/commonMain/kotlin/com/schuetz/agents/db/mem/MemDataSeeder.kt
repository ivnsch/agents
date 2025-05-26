package com.schuetz.agents.db.mem

import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.db.SeededData
import com.schuetz.agents.domain.AgentData

class MemDataSeeder : DataSeeder {
    val data = SeededData(
        SeededData.Agents(
            me = AgentData(id = 1, name = "me", isMe = true),
            dummy = AgentData(id = 2, name = "dummy", isMe = false)
        )
    )

    override suspend fun seed(): SeededData = data
}
