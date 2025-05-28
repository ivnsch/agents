package com.schuetz.agents.chat

import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatRepoImpl(
    private val messagesDao: MessagesDao,
    private val llm: LLM,
    private val dispatcher: CoroutineDispatcher
) : ChatRepo {
    override suspend fun messages(spaceId: Long): Flow<List<Message>> =
        messagesDao.messages(spaceId)

    override suspend fun sendMessage(
        message: MessageInput,
        // TODO clearer name, maybe receiver?
        agent: LLMAgent,
    ): Result<Unit> =
        llm.prompt(message.text).map { reply ->
            withContext(dispatcher) {
                messagesDao.insert(message)
                messagesDao.insert(MessageInput(reply, agent.data, message.space))
            }
        }
}
