package com.schuetz.agents.chat

import kotlinx.coroutines.flow.Flow

interface ChatRepo {
    val messages: Flow<List<Message>>

    suspend fun addMessage(message: Message)
}
