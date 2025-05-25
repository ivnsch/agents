package com.schuetz.agents.db.db

import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.db.SeededData
import com.schuetz.agents.domain.AgentInput
import com.schuetz.agents.domain.toData

class DbDataSeeder(private val agentsDao: AgentsDao) : DataSeeder {
    // TODO remove
    override fun seed(): SeededData = if (agentsDao.count() == 0L) {
        SeededData(
            agents = SeededData.Agents(
                me = agentsDao.insert(AgentInput(name = "me", isMe = true)).toData(),
                dummy = agentsDao.insert(AgentInput(name = "dummy", isMe = false)).toData()
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
