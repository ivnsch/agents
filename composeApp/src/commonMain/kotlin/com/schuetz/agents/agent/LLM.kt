package com.schuetz.agents.agent

interface LLM {
    suspend fun prompt(message: String): String
}
