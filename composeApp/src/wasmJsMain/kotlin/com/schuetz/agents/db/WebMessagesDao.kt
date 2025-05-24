package com.schuetz.agents.db

import com.schuetz.agents.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// sqldelight 2.1.0 supports wasm, but docs not updated yet
// so for now this dao with just a list
// it seems sqldelight uses anyway an in memory db on web
// so it might be needed to replace it with something else
class WebMessagesDao : MessagesDao {
    private val messages = MutableStateFlow<List<Message>>(emptyList())

    override fun all(): Flow<List<Message>> = messages

    override fun insert(message: Message) {
        messages.value += message
    }
}
