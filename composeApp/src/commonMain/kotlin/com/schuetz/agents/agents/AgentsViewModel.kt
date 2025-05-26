package com.schuetz.agents.agents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AgentsViewModel(
    private val agentsRepo: AgentsRepo,
) : ViewModel() {
    val otherAgents: Flow<List<AgentData>> = agentsRepo.otherAgents

    fun addAgent(name: String) {
        viewModelScope.launch {
            agentsRepo.addAgent(AgentInput(name, isMe = false))
        }
    }
}
