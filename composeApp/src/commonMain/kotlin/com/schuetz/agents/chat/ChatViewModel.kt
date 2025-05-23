package com.schuetz.agents.chat

import androidx.lifecycle.ViewModel

class ChatViewModel(private val chatRepo: ChatRepo) : ViewModel() {
    val messages: List<Message> = chatRepo.messages

    fun addMessage(message: Message) {
        chatRepo.addMessage(message)
    }
}

data class Message(val text: String, val author: Author)

sealed interface Author {
    data object Me : Author
    data object Agent : Author
}
