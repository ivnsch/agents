package com.schuetz.agents.domain

interface LLM {
    suspend fun prompt(message: String): Result<String>
}
