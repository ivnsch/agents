package com.schuetz.agents.chat

import androidx.compose.runtime.toMutableStateList

class ChatRepoImpl : ChatRepo {
    private val _messages: MutableList<Message> = initialMessages.toMutableStateList()
    override val messages: List<Message> = _messages

    override fun addMessage(message: Message) {
        _messages.add(0, message)
    }
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

