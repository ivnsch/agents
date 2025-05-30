package com.schuetz.agents.llm

import com.schuetz.agents.domain.LLMModelProvider

class DummyModelProvider : LLMModelProvider {
    override val models = emptyList<String>()
}
