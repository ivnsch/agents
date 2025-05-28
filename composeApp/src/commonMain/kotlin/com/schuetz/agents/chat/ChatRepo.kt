package com.schuetz.agents.chat

import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow

interface ChatRepo {
    suspend fun messages(spaceId: Long): Flow<List<Message>>

    suspend fun sendMessage(message: MessageInput): Result<Unit>
}
