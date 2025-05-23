package com.schuetz.agents.chat

import androidx.lifecycle.ViewModel
import com.schuetz.agents.agent.LLM
import com.schuetz.agents.domain.Message
import kotlinx.coroutines.flow.Flow

class ChatViewModel(
    private val chatRepo: ChatRepo,
    private val llm: LLM
) : ViewModel() {
    val messages: Flow<List<Message>> = chatRepo.messages

    suspend fun sendMessage(message: Message) {
        chatRepo.addMessage(message)
        val reply = llm.prompt(message)
        chatRepo.addMessage(reply)
    }
}
