package com.schuetz.agents.domain

import com.schuetz.agents.WeatherClient
import kotlin.Result.Companion.success

interface LLMAgentCapability {
    fun attachToPrompt(prompt: String): String
    suspend fun processResponse(response: LLMResponse): Result<StructuredMessage>?
}

// NOTE that the JSON used in these prompts has to obviously match the response classes
// it might be possible to derive the prompts from the response classes
class LLMTextMessageCapability : LLMAgentCapability {
    override fun attachToPrompt(prompt: String): String =
        """
        "by default, respond with json: 
        { 
            "type": "message",
            "text": "<YOUR REPLY>" 
        }
        """.trimIndent()

    override suspend fun processResponse(response: LLMResponse): Result<StructuredMessage>? =
        when (response) {
            is LLMResponse.MessageResponse -> success(StructuredMessage.Message(response.text))
            else -> null
        }
}

class LLMWeatherReportCapability(
    private val weatherClient: WeatherClient
) : LLMAgentCapability {
    override fun attachToPrompt(prompt: String): String =
        """
        "if I ask about the weather, respond with json (don't add anything else): 
        {
            "type": "weather"
        }
        """.trimIndent()

    override suspend fun processResponse(response: LLMResponse): Result<StructuredMessage>? =
        when (response) {
            is LLMResponse.WeatherRequest -> weatherClient.getTodaysWeather().map {
                StructuredMessage.WeatherReport(it)
            }

            else -> null
        }
}
