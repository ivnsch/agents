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
