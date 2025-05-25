package com.schuetz.agents.db

import comschuetzagents.data.AgentQueries
import comschuetzagents.data.MessageQueries

interface MyDatabase {
    val messageQueries: MessageQueries
    val agentQueries: AgentQueries
}

class MyDatabaseImpl(databaseFactory: DatabaseFactory) : MyDatabase {
    private val database = databaseFactory.create()

    override val messageQueries = database.messageQueries
    override val agentQueries = database.agentQueries
}
