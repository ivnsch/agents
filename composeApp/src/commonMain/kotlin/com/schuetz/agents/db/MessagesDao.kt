package com.schuetz.agents.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MessagesDao {
    fun all(): Flow<List<Message>>
    fun insert(message: MessageInput)
}

// TODO
// should be Dispatchers.IO, but for some reason not working on KMP
// https://github.com/Kotlin/kotlinx.coroutines/issues/3205#issuecomment-2906627080
val dispatcher = Dispatchers.Default

class MessagesDaoImpl(private val database: MyDatabase) : MessagesDao {
    override fun all(): Flow<List<Message>> =
        database.messageQueries
            .selectWithAuthor()
            .asFlow()
            .mapToList(dispatcher)
            .map { messages ->
                messages.map {
                    Message(
                        it.id,
                        it.text,
                        AgentData(id = it.author_id, name = it.author_name, isMe = it.author_is_me)
                    )
                }
            }

    override fun insert(message: MessageInput) {
        database.messageQueries.insert(message.text, message.author.id)
    }
}
