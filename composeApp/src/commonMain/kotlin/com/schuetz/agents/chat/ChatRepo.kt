package com.schuetz.agents.chat

import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow

interface ChatRepo {
    val messages: Flow<List<Message>>

    suspend fun sendMessage(message: MessageInput, agent: LLMAgent)
}
