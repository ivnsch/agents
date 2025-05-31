package com.schuetz.agents.chat

import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.StructuredMessageInput
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.flatMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class ChatRepoImpl(
    private val messagesDao: MessagesDao,
    private val llm: LLM,
    private val llmAgent: LLMAgent,
    private val dispatcher: CoroutineDispatcher
) : ChatRepo {
    override suspend fun messages(spaceId: Long): Flow<List<Message>> =
        messagesDao.messages(spaceId)

    override suspend fun sendMessage(message: MessageInput): Result<Unit> {
        addMyMessage(message)
        val modifiedPrompt = llmAgent.modifyPrompt(message.text)
        llm.prompt(modifiedPrompt).fold(
            onSuccess = { rawResponse ->
                processResponse(rawResponse, message.space).fold(
                    onSuccess = { addAgentMessage(it) },
                    onFailure = { return failure(it) }
                )
            },
            onFailure = { return failure(it) }
        )

        return success(Unit)
    }

    private suspend fun processResponse(
        rawResponse: String,
        space: SpaceData
    ): Result<StructuredMessageInput> =
        llmAgent.processResponse(rawResponse).flatMap {
            success(StructuredMessageInput(it, space.agent, space))
        }

    private suspend fun addMyMessage(message: MessageInput) {
        withContext(dispatcher) {
            messagesDao.insert(message)
        }
    }

    private suspend fun addAgentMessage(message: StructuredMessageInput) {
        withContext(dispatcher) {
            messagesDao.insert(message)
        }
    }
}
