package com.schuetz.agents.domain

import com.schuetz.agents.WeatherClientReport
import kotlinx.serialization.Serializable

interface LLMAgent {
    fun modifyPrompt(prompt: String): String
    suspend fun processResponse(response: String): Result<StructuredMessage>
}
