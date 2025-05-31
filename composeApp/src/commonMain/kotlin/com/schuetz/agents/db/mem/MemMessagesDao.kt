package com.schuetz.agents.db.mem

import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import com.schuetz.agents.domain.StructuredMessage
import com.schuetz.agents.domain.StructuredMessageInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MemMessagesDao : MessagesDao {
    private val messages = MutableStateFlow<List<Message>>(emptyList())

    override suspend fun messages(spaceId: Long): Flow<List<Message>> =
        messages.map { list ->
            list.filter { it.space.id == spaceId }
        }

    override suspend fun insert(message: MessageInput) {
        insert(
            StructuredMessageInput(
                StructuredMessage.Message(message.text),
                message.author,
                message.space
            )
        )
    }

    override suspend fun insert(message: StructuredMessageInput) {
        val newMessage =
            Message(
                id = messages.value.size.toLong(),
                content = message.content,
                author = message.author,
                space = message.space
            )
        messages.update { it + newMessage }
    }
}
