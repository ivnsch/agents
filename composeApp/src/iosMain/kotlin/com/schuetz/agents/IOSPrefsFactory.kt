package com.schuetz.agents

import com.schuetz.agents.prefs.DefaultPrefs
import com.schuetz.agents.prefs.Prefs
import com.schuetz.agents.prefs.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

class IOSPrefsFactory : PrefsFactory {

    @OptIn(ExperimentalForeignApi::class)
    override fun createPrefs(): Prefs = DefaultPrefs(createDataStore {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME"
    })
}
