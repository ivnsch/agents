package com.schuetz.agents.domain

interface LLMAgent {

    fun modifyPrompt(prompt: String): String

    fun processResponse(response: String): Result<String>
}
