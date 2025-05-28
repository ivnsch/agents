package com.schuetz.agents

import com.schuetz.agents.domain.AgentData
import kotlinx.serialization.Serializable

@Serializable
object AgentsNav

@Serializable
// nested objects appear not to work (even if AgentData is Serializable)
// https://stackoverflow.com/a/79075718/930450
// so storing the fields and converting to/from AgentData
data class ChatNav(val id: Long, val name: String, val isMe: Boolean, val avatarUrl: String)

object NavConversions {
    fun toChatNav(agent: AgentData) = ChatNav(agent.id, agent.name, agent.isMe, agent.avatarUrl)
    fun ChatNav.toAgentData() = AgentData(this.id, this.name, this.isMe, this.avatarUrl)
}
