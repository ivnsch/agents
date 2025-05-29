package com.schuetz.agents

import com.schuetz.agents.domain.AgentConnectionData.None.toConnectionData
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.SpaceData
import kotlinx.serialization.Serializable

@Serializable
object AgentsNav

@Serializable
// nested objects appear not to work (even if AgentData is Serializable)
// https://stackoverflow.com/a/79075718/930450
// so storing the fields and converting to/from AgentData
data class ChatNav(
    val id: Long,
    val name: String,
    val description: String?,
    val isMe: Boolean,
    val avatarUrl: String,
    val spaceId: Long,
    val spaceName: String,
    val agentProvider: String,
    val agentModel: String?,
    val agentApiKey: String?
)

object NavConversions {
    fun toChatNav(space: SpaceData) = ChatNav(
        space.agent.id,
        space.agent.name,
        space.agent.description,
        space.agent.isMe,
        space.agent.avatarUrl,
        space.id,
        space.name,
        space.agent.connectionData.providerStr(),
        space.agent.connectionData.modelStr(),
        space.agent.connectionData.apiKey()
    )

    fun ChatNav.toSpace(): Result<SpaceData> =
        toConnectionData(this.agentProvider, this.agentModel, this.agentApiKey)
            .map { connectionData ->
                SpaceData(
                    id = this.spaceId,
                    name = this.spaceName,
                    agent = AgentData(
                        id = this.id,
                        name = this.name,
                        description = this.description,
                        isMe = this.isMe,
                        avatarUrl = this.avatarUrl,
                        connectionData = connectionData
                    )
                )
            }
}
