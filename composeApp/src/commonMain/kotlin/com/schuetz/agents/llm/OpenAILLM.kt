package com.schuetz.agents.llm

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.RetryStrategy
import com.schuetz.agents.domain.LLM
import kotlin.time.Duration.Companion.seconds

class OpenAILLM(
    private val model: String,
    apiKey: String
) : LLM {
    private val openAI = OpenAI(
        token = apiKey,
        timeout = Timeout(socket = 10.seconds),
        retry = RetryStrategy(
            // keep it short for now for easier testing
            maxRetries = 1,
            maxDelay = 5.seconds
        )
    )

    override suspend fun prompt(message: String): Result<String> = runCatching {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId(model),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = message
                )
            )
        )
        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
        return Result.success(completion.choices.firstOrNull()?.message?.content ?: "")
    }
}
