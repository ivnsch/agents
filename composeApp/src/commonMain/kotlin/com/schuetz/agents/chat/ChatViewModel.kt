package com.schuetz.agents.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepo: ChatRepo,
    private val agent: LLMAgent,
    private val me: AgentData
) : ViewModel() {
    val messages: Flow<List<Message>> = chatRepo.messages

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: Flow<String?> = _errorMessage

    private val _isWaitingForReply = MutableStateFlow(false)
    val isWaitingForReply: Flow<Boolean> = _isWaitingForReply

    fun sendMessage(message: String) {
        viewModelScope.launch {
            _isWaitingForReply.value = true
            chatRepo.sendMessage(MessageInput(message, me), agent).onFailure { error ->
                _errorMessage.emit(error.message ?: "Unknown error")
            }
            _isWaitingForReply.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
