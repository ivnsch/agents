package com.schuetz.agents.llm

import com.schuetz.agents.domain.LLM
import kotlinx.coroutines.delay

class DummyLLM : LLM {
    override suspend fun prompt(message: String): Result<String> {
        delay(2000)
        return Result.success(randomReplies.random())
    }
}

private val randomReplies = listOf(
    "Interesting!",
    "Tell me more.",
    "I see.",
    "Why do you say that?",
    "Hmm..."
)
