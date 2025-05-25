package com.schuetz.agents.chat

import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow

class ChatRepoImpl(private val messagesDao: MessagesDao) : ChatRepo {
    override val messages: Flow<List<Message>> = messagesDao.all()

    override suspend fun addMessage(message: MessageInput) {
        messagesDao.insert(message)
    }
}
