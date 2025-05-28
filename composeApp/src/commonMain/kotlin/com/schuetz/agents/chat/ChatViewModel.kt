package com.schuetz.agents.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schuetz.agents.agents.AgentsRepo
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import com.schuetz.agents.domain.SpaceData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepo: ChatRepo,
    private val agentRepo: AgentsRepo,
    private val space: SpaceData
) : ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: Flow<String?> = _errorMessage

    private val _isWaitingForReply = MutableStateFlow(false)
    val isWaitingForReply: Flow<Boolean> = _isWaitingForReply

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: Flow<List<Message>> = _messages

    init {
        viewModelScope.launch {
            chatRepo.messages(space.id).collect { list ->
                _messages.value = list
            }
        }
    }

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
        chatRepo.sendMessage(MessageInput(message, me, space)).onFailure { error ->
            _errorMessage.emit(error.message ?: "Unknown error")
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
