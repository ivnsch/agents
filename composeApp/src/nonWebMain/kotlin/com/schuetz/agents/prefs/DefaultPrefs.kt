package com.schuetz.agents.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

class DefaultPrefs(private val dataStore: DataStore<Preferences>) : Prefs {
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

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

