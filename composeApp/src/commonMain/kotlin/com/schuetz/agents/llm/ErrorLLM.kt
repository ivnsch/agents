package com.schuetz.agents.llm

import com.schuetz.agents.domain.LLM

class ErrorLLM : LLM {
    override suspend fun prompt(message: String): Result<String> =
        Result.failure(Exception("The error LLM doesn't talk"))
}