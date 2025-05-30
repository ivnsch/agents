package com.schuetz.agents.llm

import com.schuetz.agents.client.HuggingFaceClient
import com.schuetz.agents.domain.LLM

class HuggingFaceLLM(
    private val client: HuggingFaceClient,
    private val model: String,
    private val apiKey: String
) : LLM {
    override suspend fun prompt(message: String): Result<String> =
        client.completions(message, model, apiKey)
}