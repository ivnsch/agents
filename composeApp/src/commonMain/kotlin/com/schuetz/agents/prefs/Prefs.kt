package com.schuetz.agents.prefs

import kotlinx.coroutines.flow.Flow

interface Prefs {
    suspend fun saveString(key: PrefsKey, value: String)
    suspend fun getString(key: PrefsKey): Flow<String?>
}

enum class PrefsKey(val str: String) {
    HUGGING_FACE_TOKEN("huggingface_token")
}
