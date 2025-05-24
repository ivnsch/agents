package com.schuetz.agents.chat

import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.Message
import kotlinx.coroutines.flow.Flow

class ChatRepoImpl(private val messagesDao: MessagesDao) : ChatRepo {
    override val messages: Flow<List<Message>> = messagesDao.all()

    override suspend fun addMessage(message: Message) {
        messagesDao.insert(message)
    }
}
