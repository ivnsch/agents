package com.schuetz.agents.domain

import kotlinx.serialization.Serializable

class LLMAgentImpl(
    private val formatMediator: LLMFormatMediator
) : LLMAgent {
    override fun modifyPrompt(prompt: String): String =
        formatMediator.modifyPrompt(prompt)


    override fun processResponse(response: String): Result<String> =
        formatMediator.processResponse(response)
}

@Serializable
data class MessageResponse(val text: String)
