package com.schuetz.agents.huggingface

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HuggingFaceTokenStore {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token

    fun update(token: String?) {
        _token.value = token
    }
}
