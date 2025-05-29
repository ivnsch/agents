package com.schuetz.agents.domain

class ErrorLLM : LLM {
    override suspend fun prompt(message: String): Result<String> =
        Result.failure(Exception("The error LLM doesn't talk"))
}
