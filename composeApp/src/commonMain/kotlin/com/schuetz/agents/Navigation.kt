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
        space.agent.isMe,
        space.agent.avatarUrl,
        space.id,
        space.name,
        space.agent.connectionData.providerStr(),
        space.agent.connectionData.modelStr(),
        space.agent.connectionData.apiKey()
    )

    fun ChatNav.toSpace() = SpaceData(
        this.spaceId,
        this.spaceName,
        AgentData(
            this.id,
            this.name,
            this.isMe,
            this.avatarUrl,
            toConnectionData(this.agentProvider, this.agentModel, this.agentApiKey)
        )
    )
}
