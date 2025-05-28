package com.schuetz.agents.domain

interface LLM {
    // TODO review design: api key should be an implementation detail for LLMs that require one
    suspend fun prompt(message: String, apiKey: String?): Result<String>
}
