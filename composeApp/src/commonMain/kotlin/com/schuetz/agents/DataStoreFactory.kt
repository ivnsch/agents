package com.schuetz.agents

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

interface DataStoreFactory {
    fun createDataStore(): DataStore<Preferences>
}

// Note: crashes (at least on Android) if this extension is not used
private const val REQUIRED_PREFS_EXTENSION = "preferences_pb"

const val DATA_STORE_FILE_NAME = "prefs.$REQUIRED_PREFS_EXTENSION"

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
