package com.schuetz.agents.domain

import com.schuetz.agents.agent.LLM
import comschuetzagents.data.Agent

data class LLMAgent(val data: AgentData, private val llm: LLM) {
    suspend fun prompt(message: String) = llm.prompt(message)
}

data class AgentData(val id: Long, val name: String, val isMe: Boolean)

data class AgentInput(val name: String, val isMe: Boolean)

fun Agent.toData() = AgentData(id, name, is_me)
