package com.schuetz.agents.db.db

import app.cash.sqldelight.TransactionWithReturn
import com.schuetz.agents.Database
import comschuetzagents.data.AgentQueries
import comschuetzagents.data.MessageQueries
import comschuetzagents.data.SpaceQueries

interface MyDatabase {
    val messageQueries: MessageQueries
    val agentQueries: AgentQueries
    val spaceQueries: SpaceQueries

    fun <T> transactionWithResult(bodyWithReturn: TransactionWithReturn<T>.() -> T): T
}

class MyDatabaseImpl(databaseFactory: DatabaseFactory) : MyDatabase {
    private val database: Database = databaseFactory.create()

    override val messageQueries = database.messageQueries
    override val agentQueries = database.agentQueries
    override val spaceQueries = database.spaceQueries

    override fun <T> transactionWithResult(bodyWithReturn: TransactionWithReturn<T>.() -> T): T =
        database.transactionWithResult(false) {
            bodyWithReturn()
        }
}
