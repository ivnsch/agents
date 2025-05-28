package com.schuetz.agents.domain

import kotlinx.serialization.Serializable

data class LLMAgent(val data: AgentData, private val llm: LLM) {
    suspend fun prompt(message: String) = llm.prompt(message)
}

@Serializable
data class AgentData(val id: Long, val name: String, val isMe: Boolean, val avatarUrl: String)

data class AgentInput(val name: String, val isMe: Boolean, val avatarUrl: String)
