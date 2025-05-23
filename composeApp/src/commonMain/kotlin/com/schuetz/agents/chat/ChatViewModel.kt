package com.schuetz.agents.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class ChatViewModel(private val chatRepo: ChatRepo) : ViewModel() {
    val messages: Flow<List<Message>> = chatRepo.messages

    suspend fun addMessage(message: Message) {
        chatRepo.addMessage(message)
    }
}

data class Message(val text: String, val author: Author)

sealed interface Author {
    data object Me : Author
    data object Agent : Author
}
