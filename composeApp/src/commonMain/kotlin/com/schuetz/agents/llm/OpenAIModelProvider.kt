package com.schuetz.agents.llm

import com.schuetz.agents.domain.LLMModelProvider

class OpenAIModelProvider : LLMModelProvider {
    override val models: List<String> = listOf(
        "gpt-4.1", "gpt-4.1-mini", "gpt-4.1-nano", "gpt-4o", "gpt-4o-mini", "gpt-3.5-turbo"
    )
}
