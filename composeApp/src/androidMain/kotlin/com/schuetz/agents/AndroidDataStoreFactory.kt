package com.schuetz.agents

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class AndroidDataStoreFactory(private val context: Context) : DataStoreFactory {
    override fun createDataStore(): DataStore<Preferences> = createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}
