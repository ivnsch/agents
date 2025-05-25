package com.schuetz.agents.chat

import androidx.lifecycle.ViewModel
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow

class ChatViewModel(
    private val chatRepo: ChatRepo,
    private val agent: LLMAgent,
    private val me: AgentData
) : ViewModel() {
    val messages: Flow<List<Message>> = chatRepo.messages

    suspend fun sendMessage(message: String) {
        chatRepo.addMessage(MessageInput(message, me))
        val reply = agent.prompt(message)
        chatRepo.addMessage(MessageInput(reply, agent.data))
    }
}
