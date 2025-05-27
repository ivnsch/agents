package com.schuetz.agents.chat

import androidx.lifecycle.ViewModel
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel(
    private val chatRepo: ChatRepo,
    private val agent: LLMAgent,
    private val me: AgentData
) : ViewModel() {
    val messages: Flow<List<Message>> = chatRepo.messages

    private val _isWaitingForReply = MutableStateFlow(false)
    val isWaitingForReply: Flow<Boolean> = _isWaitingForReply

    suspend fun sendMessage(message: String) {
        _isWaitingForReply.value = true
        chatRepo.sendMessage(MessageInput(message, me), agent)
        _isWaitingForReply.value = false
    }
}
