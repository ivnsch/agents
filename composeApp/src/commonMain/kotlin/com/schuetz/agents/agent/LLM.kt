package com.schuetz.agents.agent

import com.schuetz.agents.domain.Message

interface LLM {
    suspend fun prompt(message: Message): Message
}
