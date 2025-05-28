package com.schuetz.agents.domain

import com.schuetz.agents.huggingface.HuggingFaceClient

class HuggingFaceLLM(private val client: HuggingFaceClient) : LLM {
    override suspend fun prompt(message: String, apiKey: String?): Result<String> =
        apiKey?.let {
            client.completions(message, apiKey)
        } ?: Result.failure(Exception("No api key provided for hugging face provider"))
}
