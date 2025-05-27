package com.schuetz.agents.domain

import com.schuetz.agents.huggingface.HuggingFaceClient

class HuggingFaceLLM(private val client: HuggingFaceClient) : LLM {
    override suspend fun prompt(message: String): Result<String> =
        client.completions(message)
}
