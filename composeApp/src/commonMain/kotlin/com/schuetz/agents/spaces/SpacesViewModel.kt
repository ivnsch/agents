package com.schuetz.agents.spaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schuetz.agents.AvatarUrlGenerator
import com.schuetz.agents.domain.AgentConnectionData.None.toConnectionData
import com.schuetz.agents.domain.AgentInput
import com.schuetz.agents.domain.ConnectableProvider
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.domain.SpaceInput
import com.schuetz.agents.huggingface.huggingFaceModelNames
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SpacesViewModel(
    private val spacesRepo: SpacesRepo,
    private val avatarUrlGenerator: AvatarUrlGenerator,
) : ViewModel() {
    val spaces: Flow<List<SpaceData>> = spacesRepo.spaces

    private val _newAgentavatarUrl = MutableStateFlow(avatarUrlGenerator.generateRandomAvatarUrl())
    val newAgentavatarUrl: StateFlow<String> = _newAgentavatarUrl

    // When the user adds an agent, add automatically a space
    fun addSpaceWithAgent(inputs: AddAgentInputs) {
        addSpace(AddSpaceInputs(inputs.name, inputs))
    }

    private fun addSpace(inputs: AddSpaceInputs) {
        viewModelScope.launch {
            spacesRepo.addSpace(
                SpaceInput(
                    inputs.name,
                    AgentInput(
                        inputs.name, isMe = false, _newAgentavatarUrl.value, toConnectionData(
                            inputs.agent.provider,
                            inputs.agent.model,
                            inputs.agent.apiKey,
                        )
                    )
                )
            )
        }
    }

    fun regenerateAvatarUrl() {
        _newAgentavatarUrl.value = avatarUrlGenerator.generateRandomAvatarUrl()
    }

    fun llmModels(): List<String> =
        // TODO generalize
        huggingFaceModelNames
}

data class AddSpaceInputs(val name: String, val agent: AddAgentInputs)
data class AddAgentInputs(
    val name: String,
    val provider: ConnectableProvider,
    val model: String?,
    val apiKey: String?,
    val avatarUrl: String
)
