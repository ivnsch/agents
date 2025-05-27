package com.schuetz.agents

import com.schuetz.agents.prefs.DefaultPrefs
import com.schuetz.agents.prefs.Prefs
import com.schuetz.agents.prefs.createDataStore

class DesktopPrefsFactory : PrefsFactory {
    override fun createPrefs(): Prefs = DefaultPrefs(createDataStore {
        DATA_STORE_FILE_NAME
    })
}
