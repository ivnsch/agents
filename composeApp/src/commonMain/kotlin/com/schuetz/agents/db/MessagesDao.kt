package com.schuetz.agents.db

import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow

interface MessagesDao {
    suspend fun messages(spaceId: Long): Flow<List<Message>>
    suspend fun insert(message: MessageInput)
}
