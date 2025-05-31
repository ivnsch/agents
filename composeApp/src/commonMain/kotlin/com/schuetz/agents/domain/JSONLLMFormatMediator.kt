package com.schuetz.agents.domain

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.Result.Companion.success

interface LLMFormatMediator {
    fun modifyPrompt(prompt: String): String
    fun processResponse(response: String): Result<LLMResponse>
}

class JSONLLMFormatMediator : LLMFormatMediator {
    override fun modifyPrompt(prompt: String): String =
        addJsonRequest(prompt)

    override fun processResponse(response: String): Result<LLMResponse> {
        val json = Json { classDiscriminator = "type" }
        return runCatching {
            val decoded = json.decodeFromString<LLMResponse>(response)
            return success(decoded)
        }
    }

    private fun addJsonRequest(prompt: String): String =
        """
            "Please respond in json format: { \"text\": \"<YOUR REPLY>\" }"
            $prompt
        """.trimIndent()

}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class LLMResponse {
    @Serializable
    @SerialName("message")
    data class MessageResponse(val text: String) : LLMResponse()

    @Serializable
    @SerialName("weather")
    data object WeatherRequest : LLMResponse()
}
