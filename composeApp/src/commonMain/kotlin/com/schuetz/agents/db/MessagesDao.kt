package com.schuetz.agents.db

import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow

interface MessagesDao {
    fun all(): Flow<List<Message>>
    fun insert(message: MessageInput)
}

