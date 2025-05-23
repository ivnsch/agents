package com.schuetz.agents.chat

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    private val _messages: MutableList<Message> = initialMessages.toMutableStateList()
    val messages: List<Message> = _messages

    fun addMessage(message: Message) {
        _messages.add(0, message)
    }
}

data class Message(val text: String, val author: Author)

sealed interface Author {
    data object Me : Author
    data object Agent : Author
}

private val initialMessages = listOf(
    Message("Hello!", Author.Me),
    Message("hi!", Author.Agent),
    Message("how are you doing?", Author.Me),
    Message("I'm doing great, thanks!", Author.Agent),
    Message("I'm doing great, thanks!", Author.Me),
    Message("I'm doing great, thanks!", Author.Agent),
    Message(
        "I'm doing great, thanks! I'm doing great, thanks! I'm doing great, thanks! I'm doing great, thanks!",
        Author.Me
    ),
    Message("I'm doing great, thanks!", Author.Agent),
    Message("I'm doing great, thanks!", Author.Me),
    Message(
        "I'm doing great, thanks!, I'm doing great, thanks!, I'm doing great, thanks!, I'm doing great, thanks!, I'm doing great, thanks!",
        Author.Agent
    ),
    Message("I'm doing great, thanks!", Author.Me),
    Message("I'm doing great, thanks!", Author.Agent),
    Message("I'm doing great, thanks!", Author.Me),
).reversed()
