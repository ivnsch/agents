package com.schuetz.agents.db

import comschuetzagents.data.MessageQueries

interface MyDatabase {
    val messageQueries: MessageQueries
}

class MyDatabaseImpl(databaseFactory: DatabaseFactory) : MyDatabase {
    private val database = databaseFactory.create()

    override val messageQueries = database.messageQueries
}
