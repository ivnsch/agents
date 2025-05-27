package com.schuetz.agents.huggingface

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
    suspend fun completions(prompt: String): String
}

class HuggingFaceClientImpl(
    private val client: HttpClient,
    private val tokenStore: HuggingFaceTokenStore
) : HuggingFaceClient {
    override suspend fun completions(prompt: String): String {
        // TODO error handling
        val authToken = tokenStore.token.value ?: ""

        val response = client.post("https://router.huggingface.co/cerebras/v1/chat/completions") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $authToken")
                contentType(ContentType.Application.Json)
                setBody(
                    CompletionsRequest(
                        messages = listOf(
                            Message(role = "user", content = prompt)
                        ),
                        // hardcoding the model for now
                        model = "qwen-3-32b"
                    )
                )
            }
        }

        val choices = response.body<CompletionsResponse>().choices
        // TODO Result type
        return choices.firstOrNull()?.message?.content ?: error("No choices in response")
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
