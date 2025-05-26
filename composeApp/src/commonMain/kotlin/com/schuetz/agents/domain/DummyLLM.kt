package com.schuetz.agents.domain

import kotlinx.coroutines.delay

class DummyLLM : LLM {
    override suspend fun prompt(message: String): String {
        delay(2000)
        return randomReplies.random()
    }
}

private val randomReplies = listOf(
    "Interesting!",
    "Tell me more.",
    "I see.",
    "Why do you say that?",
    "Hmm..."
)
