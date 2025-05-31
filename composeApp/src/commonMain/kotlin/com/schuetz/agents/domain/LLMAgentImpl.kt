package com.schuetz.agents.domain

class LLMAgentImpl(
    private val formatMediator: LLMFormatMediator,
    defaultCapability: LLMAgentCapability,
    additionalCapabilities: List<LLMAgentCapability>
) : LLMAgent {
    private val capabilities =
        listOf(defaultCapability) + additionalCapabilities

    override fun modifyPrompt(prompt: String): String =
        capabilities.joinToString("\n") { it.attachToPrompt("") } + "\n" + prompt

    override suspend fun processResponse(response: String): Result<StructuredMessage> =
        formatMediator.processResponse(response).fold(
            onSuccess = { text ->
                capabilities
                    // the response is consumed by the first capability that matches
                    .firstNotNullOfOrNull { it.processResponse(text) }
                    ?: Result.failure(Exception("No capability handled response"))
            },
            onFailure = { Result.failure(it) }
        )
}
