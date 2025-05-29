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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SpacesViewModel(
    private val spacesRepo: SpacesRepo,
    private val avatarUrlGenerator: AvatarUrlGenerator,
) : ViewModel() {
    val spaces: Flow<List<SpaceData>> = spacesRepo.spaces
        .catch { _errorMessage.value = it.toString() }

    private val _newAgentavatarUrl = MutableStateFlow(avatarUrlGenerator.generateRandomAvatarUrl())
    val newAgentavatarUrl: StateFlow<String> = _newAgentavatarUrl

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asSharedFlow()

    // When the user adds an agent, add automatically a space
    fun addSpaceWithAgent(inputs: AddAgentInputs) {
        addSpace(AddSpaceInputs(inputs.name, inputs))
    }

    private fun addSpace(inputs: AddSpaceInputs) {
        viewModelScope.launch {
            val connectionData = toConnectionData(
                inputs.agent.provider,
                inputs.agent.model,
                inputs.agent.apiKey,
            ).getOrElse {
                _errorMessage.emit(it.toString())
                return@launch
            }

            spacesRepo.addSpace(
                SpaceInput(
                    inputs.name,
                    AgentInput(
                        inputs.name,
                        inputs.agent.description,
                        isMe = false,
                        _newAgentavatarUrl.value,
                        connectionData
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

    fun clearError() {
        _errorMessage.value = null
    }
}

data class AddSpaceInputs(val name: String, val agent: AddAgentInputs)
data class AddAgentInputs(
    val name: String,
    val description: String?,
    val provider: ConnectableProvider,
    val model: String?,
    val apiKey: String?,
    val avatarUrl: String
)
