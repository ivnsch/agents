package com.schuetz.agents.domain

/**
 *  Dedicated interface to retrieve LLM models
 *
 *  this is not part of LLM because we don't want to require an API token
 *  mainly due to UX reasons:
 *  we ask the user for the API token and to select a model in the same form
 *
 *  Also, the API token has limited/no use here:
 *  Some providers like HuggingFace don't seem to have an endpoint to return models
 *  so we have to hardcode them
 *  OpenAI has one, but it returns currently 50 or so, with no usable filtering criteria
 *  so we've to hardcode them either way
 */
interface LLMModelProvider {
    val models: List<String>
}
