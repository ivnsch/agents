package com.schuetz.agents.agents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schuetz.agents.AvatarUrlGenerator
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.AgentInput
import com.schuetz.agents.huggingface.HuggingFaceTokenStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AgentsViewModel(
    private val agentsRepo: AgentsRepo,
    avatarUrlGenerator: AvatarUrlGenerator,
    // TODO we need a generic solution for client-specific stores
    private val huggingFaceTokenStore: HuggingFaceTokenStore
) : ViewModel() {
    val otherAgents: Flow<List<AgentData>> = agentsRepo.otherAgents
    val avatarUrl = avatarUrlGenerator.generateRandomAvatarUrl()

    fun addAgent(inputs: AddAgentInputs) {
        viewModelScope.launch {
            agentsRepo.addAgent(AgentInput(inputs.name, isMe = false, avatarUrl))
            huggingFaceTokenStore.update(inputs.authToken)
        }
    }
}

data class AddAgentInputs(val name: String, val authToken: String, val avatarUrl: String)
