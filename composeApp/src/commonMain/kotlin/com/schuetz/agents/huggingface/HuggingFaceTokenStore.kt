package com.schuetz.agents.huggingface

import com.schuetz.agents.prefs.Prefs
import com.schuetz.agents.prefs.PrefsKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class HuggingFaceTokenStore(
    private val prefs: Prefs,
    private val dispatcher: CoroutineDispatcher
) {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token

    suspend fun initialize(): Unit = withContext(dispatcher) {
        prefs.getString(PrefsKey.HUGGING_FACE_TOKEN).collect {
            _token.value = it
        }
    }

    suspend fun update(token: String): Unit = withContext(dispatcher) {
        prefs.saveString(PrefsKey.HUGGING_FACE_TOKEN, token)
        _token.value = token
    }
}
