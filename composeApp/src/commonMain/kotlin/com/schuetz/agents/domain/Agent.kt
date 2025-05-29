package com.schuetz.agents.domain

import kotlinx.serialization.Serializable

@Serializable
data class AgentData(
    val id: Long,
    val name: String,
    val isMe: Boolean,
    val avatarUrl: String,
    val connectionData: AgentConnectionData
)

data class AgentInput(
    val name: String,
    val isMe: Boolean,
    val avatarUrl: String,
    val connectionData: AgentConnectionData
)

// TODO review, feels convoluted
sealed interface AgentConnectionData {
    data class HuggingFace(val accessToken: String) : AgentConnectionData
    data object Dummy : AgentConnectionData
    data object None : AgentConnectionData

    companion object {
        private const val HUGGING_FACE_STR = "huggingface"
        private const val NONE_STR = "none"
        private const val DUMMY_STR = "dummy"
    }

    fun providerStr(): String = when (this) {
        is HuggingFace -> HUGGING_FACE_STR
        is Dummy -> DUMMY_STR
        is None -> NONE_STR
    }

    fun apiKey(): String? = when (this) {
        is HuggingFace -> accessToken
        is None, Dummy -> null
    }

    fun toConnectionData(provider: String, apiKey: String?): AgentConnectionData =
        when (provider) {
            HUGGING_FACE_STR -> apiKey?.let { HuggingFace(apiKey) }
                ?: throw IllegalArgumentException("Missing api key for Hugging Face provider")

            DUMMY_STR -> Dummy
            NONE_STR -> None
            else -> throw IllegalArgumentException("Unknown provider: $provider")
        }

    fun toConnectionData(provider: ConnectableProvider, apiKey: String?): AgentConnectionData =
        toConnectionData(toProviderString(provider), apiKey)

    fun toProviderString(connectableProvider: ConnectableProvider) = when (connectableProvider) {
        ConnectableProvider.HUGGING_FACE -> HUGGING_FACE_STR
        ConnectableProvider.DUMMY -> DUMMY_STR
    }
}

enum class ConnectableProvider {
    HUGGING_FACE, DUMMY
}

