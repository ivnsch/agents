package com.schuetz.agents.domain

import com.schuetz.agents.huggingface.HuggingFaceClient

class HuggingFaceLLM(private val client: HuggingFaceClient) : LLM {
    override suspend fun prompt(message: String): String =
        client.completions(message)
}
