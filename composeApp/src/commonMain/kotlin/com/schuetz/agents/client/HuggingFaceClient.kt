package com.schuetz.agents.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

interface HuggingFaceClient {
    suspend fun completions(prompt: String, model: String, accessToken: String): Result<String>
}

class HuggingFaceClientImpl(
    private val client: HttpClient,
) : HuggingFaceClient {
    override suspend fun completions(
        prompt: String,
        model: String,
        accessToken: String
    ): Result<String> {
        val response = client.post("https://router.huggingface.co/cerebras/v1/chat/completions") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
                contentType(ContentType.Application.Json)
                setBody(
                    CompletionsRequest(
                        messages = listOf(
                            Message(role = "user", content = prompt)
                        ),
                        model = model
                    )
                )
            }
        }

        return runCatching {
            val choices = response.body<CompletionsResponse>().choices
            choices.firstOrNull()?.message?.content
                ?: throw Exception("No choices found in completion response")
        }
    }
}

@Serializable
data class CompletionsRequest(val messages: List<Message>, val model: String)

@Serializable
data class Message(val role: String, val content: String)

@Serializable
data class CompletionsResponse(val choices: List<CompletionsChoice>)

@Serializable
data class CompletionsChoice(val message: Message)

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
private val cerebrasModelNames = listOf(
    "llama3.1-8b",
    "qwen-3-32b",
    "llama-4-scout-17b-16e-instruct",
    "llama-3.3-70b",
)

// for now hardcoded to cerebras provider
val huggingFaceModelNames = cerebrasModelNames
