package com.schuetz.agents.domain

import kotlinx.serialization.json.Json

interface LLMFormatMediator {
    fun modifyPrompt(prompt: String): String
    fun processResponse(response: String): Result<String>
}

class JSONLLMFormatMediator : LLMFormatMediator {
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
