package com.schuetz.agents.domain

import com.schuetz.agents.WeatherClientReport
import kotlinx.serialization.Serializable

data class MessageInput(val text: String, val author: AgentData, val space: SpaceData)

data class StructuredMessageInput(
    val content: StructuredMessage,
    val author: AgentData,
    val space: SpaceData
)

data class Message(
    val id: Long,
    val content: StructuredMessage,
    val author: AgentData,
    val space: SpaceData
)

@Serializable
sealed interface StructuredMessage {
    @Serializable
    data class Message(val text: String) : StructuredMessage

    @Serializable
    data class WeatherReport(val report: WeatherClientReport) : StructuredMessage
}
