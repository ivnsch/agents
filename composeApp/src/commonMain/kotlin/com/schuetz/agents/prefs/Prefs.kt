package com.schuetz.agents.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.schuetz.agents.DataStoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface Prefs {
    suspend fun saveString(key: PrefsKey, value: String)
    suspend fun getString(key: PrefsKey): Flow<String?>
}

enum class PrefsKey(val str: String) {
    HUGGING_FACE_TOKEN("huggingface_token")
}

class PrefsImpl(dataStoreFactory: DataStoreFactory) : Prefs {
    private val dataStore = dataStoreFactory.createDataStore()

    override suspend fun saveString(key: PrefsKey, value: String) {
        val prefKey = stringPreferencesKey(key.str)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }

    override suspend fun getString(key: PrefsKey): Flow<String?> {
        val prefKey = stringPreferencesKey(key.str)
        return dataStore.data.map { prefs ->
            prefs[prefKey]
        }
    }
}
