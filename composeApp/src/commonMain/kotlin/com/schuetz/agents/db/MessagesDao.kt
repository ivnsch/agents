package com.schuetz.agents.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.schuetz.agents.domain.Author
import com.schuetz.agents.domain.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MessagesDao {
    fun all(): Flow<List<Message>>
    fun insert(message: Message)
}

// TODO
// should be Dispatchers.IO, but for some reason not working on KMP
// https://github.com/Kotlin/kotlinx.coroutines/issues/3205#issuecomment-2906627080
val dispatcher = Dispatchers.Default

class MessagesDaoImpl(private val database: MyDatabase) : MessagesDao {
    override fun all(): Flow<List<Message>> =
        database.messageQueries
            .selectAll()
            .asFlow()
            .mapToList(dispatcher)
            .map { messages ->
                messages.map {
                    Message(it.text, toAuthor(it.author))
                }
            }

    override fun insert(message: Message) {
        database.messageQueries.insert(message.text, message.author.toDbStr())
    }
}

private fun toAuthor(dbStr: String) = when (dbStr) {
    AUTHOR_ME_IDENTIFIER -> Author.Me
    AUTHOR_AGENT_IDENTIFIER -> Author.Agent
    else -> error("Invalid author id: $dbStr")
}

private fun Author.toDbStr() = when (this) {
    Author.Agent -> AUTHOR_AGENT_IDENTIFIER
    Author.Me -> AUTHOR_ME_IDENTIFIER
}

// with some noise at the end to avoid (unlikely) collisions with agent names
private const val AUTHOR_ME_IDENTIFIER = "me_##"

// later this will be the specific agent name
private const val AUTHOR_AGENT_IDENTIFIER = "agent"
