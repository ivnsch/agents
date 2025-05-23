package com.schuetz.agents.chat

import com.schuetz.agents.domain.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepo {
    val messages: Flow<List<Message>>

    suspend fun addMessage(message: Message)
}
