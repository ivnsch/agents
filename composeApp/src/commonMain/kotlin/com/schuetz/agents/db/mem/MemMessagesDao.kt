package com.schuetz.agents.db.mem

import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MemMessagesDao : MessagesDao {
    private val messages = MutableStateFlow<List<Message>>(emptyList())

    override val all: Flow<List<Message>> = messages

    override suspend fun insert(message: MessageInput) {
        val newMessage =
            Message(id = messages.value.size.toLong(), text = message.text, author = message.author)
        messages.update { it + newMessage }
    }
}
