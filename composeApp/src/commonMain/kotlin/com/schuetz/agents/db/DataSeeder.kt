package com.schuetz.agents.db

import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import com.schuetz.agents.domain.toData

// TODO temporary implementation, this data will come from user flows
interface DataSeeder {
    // TODO remove
    fun seed(): SeededData
}

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

data class SeededData(val agents: Agents) {
    data class Agents(val me: AgentData, val dummy: AgentData)
}
