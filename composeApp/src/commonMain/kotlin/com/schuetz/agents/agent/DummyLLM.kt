package com.schuetz.agents.agent

import com.schuetz.agents.domain.Author
import com.schuetz.agents.domain.Message
import kotlinx.coroutines.delay

class DummyLLM : LLM {
    override suspend fun prompt(message: Message): Message {
        delay(1000)
        return Message(text = randomReplies.random(), author = Author.Agent)
    }
}

private val randomReplies = listOf(
    "Interesting!",
    "Tell me more.",
    "I see.",
    "Why do you say that?",
    "Hmm..."
)
