package com.schuetz.agents.domain

import com.schuetz.agents.huggingface.HuggingFaceClient

class HuggingFaceLLM(
    private val client: HuggingFaceClient,
    private val model: String,
    private val apiKey: String
) : LLM {
    override suspend fun prompt(message: String): Result<String> =
        client.completions(message, model, apiKey)
}
