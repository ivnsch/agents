package com.schuetz.agents

import com.schuetz.agents.prefs.Prefs
import com.schuetz.agents.prefs.PrefsKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// For now just in-memory prefs for web
class WebPrefs : Prefs {
    private val data = mutableMapOf<String, MutableStateFlow<String?>>()

    override suspend fun saveString(key: PrefsKey, value: String) {
        data.getOrPut(key.str) { MutableStateFlow(value) }.value = value
    }

    override suspend fun getString(key: PrefsKey): Flow<String?> =
        data.getOrPut(key.str) { MutableStateFlow(null) }.asStateFlow()
}
