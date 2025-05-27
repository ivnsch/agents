package com.schuetz.agents.chat

import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow

class ChatRepoImpl(
    private val messagesDao: MessagesDao,
    private val llm: LLM
) : ChatRepo {
    override val messages: Flow<List<Message>> = messagesDao.all

    override suspend fun sendMessage(message: MessageInput, agent: LLMAgent): Result<Unit> =
        llm.prompt(message.text).map { reply ->
            messagesDao.insert(message)
            messagesDao.insert(MessageInput(reply, agent.data))
        }
}
