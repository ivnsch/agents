package com.schuetz.agents

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class DesktopDataStoreFactory : DataStoreFactory {
    override fun createDataStore(): DataStore<Preferences> = createDataStore {
        DATA_STORE_FILE_NAME
    }
}
