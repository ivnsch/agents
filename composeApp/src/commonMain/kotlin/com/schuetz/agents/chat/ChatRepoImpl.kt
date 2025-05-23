package com.schuetz.agents.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ChatRepoImpl : ChatRepo {
    private val _messages = MutableStateFlow(initialMessages)
    override val messages: Flow<List<Message>> = _messages

    override suspend fun addMessage(message: Message) {
        _messages.value = listOf(message) + _messages.value
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

