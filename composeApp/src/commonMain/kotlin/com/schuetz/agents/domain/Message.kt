package com.schuetz.agents.domain

data class MessageInput(val text: String, val author: AgentData, val space: SpaceData)

data class Message(val id: Long, val text: String, val author: AgentData, val space: SpaceData)
