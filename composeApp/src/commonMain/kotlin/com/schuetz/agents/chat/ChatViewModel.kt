package com.schuetz.agents.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schuetz.agents.agents.AgentsRepo
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepo: ChatRepo,
    private val agent: LLMAgent,
    private val agentRepo: AgentsRepo
) : ViewModel() {
    val messages: Flow<List<Message>> = chatRepo.messages

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: Flow<String?> = _errorMessage

    private val _isWaitingForReply = MutableStateFlow(false)
    val isWaitingForReply: Flow<Boolean> = _isWaitingForReply

    fun sendMessage(message: String) {
        viewModelScope.launch {
            _isWaitingForReply.value = true
            agentRepo.me
                .catch { _errorMessage.emit(it.toString()) }
                .firstOrNull()?.let {
                    sendMessage(message, it)
                } ?: _errorMessage.emit("No 'me' agent found.")
            _isWaitingForReply.value = false
        }
    }

    private suspend fun sendMessage(message: String, me: AgentData) {
        chatRepo.sendMessage(MessageInput(message, me), agent).onFailure { error ->
            _errorMessage.emit(error.message ?: "Unknown error")
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
