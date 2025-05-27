package com.schuetz.agents

import android.content.Context
import com.schuetz.agents.prefs.DefaultPrefs
import com.schuetz.agents.prefs.Prefs
import com.schuetz.agents.prefs.createDataStore

class AndroidPrefsFactory(private val context: Context) : PrefsFactory {
    override fun createPrefs(): Prefs = DefaultPrefs(createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    })
}
