package com.schuetz.agents.db

import com.schuetz.agents.domain.AgentData

// TODO temporary implementation, this data will come from user flows
interface DataSeeder {
    // TODO remove
    fun seed(): SeededData
}

data class SeededData(val agents: Agents) {
    data class Agents(val me: AgentData, val dummy: AgentData)
}
