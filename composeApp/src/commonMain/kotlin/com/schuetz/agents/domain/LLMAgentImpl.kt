package com.schuetz.agents.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LLMAgentImpl : LLMAgent {
    override fun modifyPrompt(prompt: String): String =
        addJsonRequest(prompt)


    override fun processResponse(response: String): Result<String> =
        toMessageResponse(response).map {
            it.text
        }

    private fun addJsonRequest(prompt: String): String =
        """
            "Please respond in json format: { \"text\": \"<YOUR REPLY>\" }"
            $prompt
        """.trimIndent()

    private fun toMessageResponse(response: String): Result<MessageResponse> {
        val json = Json { ignoreUnknownKeys = true }
        return runCatching {
            json.decodeFromString<MessageResponse>(response)
        }
    }
}

@Serializable
data class MessageResponse(val text: String)
