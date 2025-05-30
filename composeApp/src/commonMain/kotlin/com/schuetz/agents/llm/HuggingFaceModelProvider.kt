package com.schuetz.agents.llm

import com.schuetz.agents.domain.LLMModelProvider

class HuggingFaceModelProvider : LLMModelProvider {
    override val models = cerebrasModels
}

// NOTE that the hugging face model names (e.g. Qwen/Qwen3-32B instead of qwen-3-32b)
// don't work here, we've to use Cerebras specific identifiers:
// https://inference-docs.cerebras.ai/api-reference/chat-completions
//
// why does hugging face provide a different endpoint for "requests" client than
// apparently when using huggingface_hub, which does accept the hugging face model names?
// see https://huggingface.co/docs/inference-providers/en/providers/cerebras
// (client "huggingface_hub" vs. client "requests")
//
// TODO ideally use a REST interface that works with hugging face model names
private val cerebrasModels = listOf(
    "llama3.1-8b",
    "qwen-3-32b",
    "llama-4-scout-17b-16e-instruct",
    "llama-3.3-70b",
)
