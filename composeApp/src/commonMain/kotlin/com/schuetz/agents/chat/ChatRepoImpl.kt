package com.schuetz.agents.chat

import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import com.schuetz.agents.flatMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatRepoImpl(
    private val messagesDao: MessagesDao,
    private val llm: LLM,
    private val llmAgent: LLMAgent,
    private val dispatcher: CoroutineDispatcher
) : ChatRepo {
    override suspend fun messages(spaceId: Long): Flow<List<Message>> =
        messagesDao.messages(spaceId)

    override suspend fun sendMessage(message: MessageInput): Result<Unit> {
        addMessage(message)
        val modifiedPrompt = llmAgent.modifyPrompt(message.text)
        return llm.prompt(modifiedPrompt)
            .flatMap { llmAgent.processResponse(it) }
            .map { processedResponse ->
                addMessage(MessageInput(processedResponse, message.space.agent, message.space))
            }
    }

    private suspend fun addMessage(message: MessageInput) {
        withContext(dispatcher) {
            messagesDao.insert(message)
        }
    }
}
