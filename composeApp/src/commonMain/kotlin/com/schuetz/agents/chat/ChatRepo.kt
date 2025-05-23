package com.schuetz.agents.chat

interface ChatRepo {
    val messages: List<Message>

    fun addMessage(message: Message)
}
