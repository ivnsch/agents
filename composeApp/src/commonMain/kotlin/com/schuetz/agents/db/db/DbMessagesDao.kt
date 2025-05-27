package com.schuetz.agents.db.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class DbMessagesDao(
    private val database: MyDatabase,
    dispatcher: CoroutineDispatcher
) : MessagesDao {
    override val all: Flow<List<Message>> =
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
            .flowOn(dispatcher)

    override suspend fun insert(message: MessageInput) {
        database.messageQueries.insert(message.text, message.author.id)
    }
}
