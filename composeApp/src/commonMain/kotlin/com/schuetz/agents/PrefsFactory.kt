package com.schuetz.agents

import com.schuetz.agents.prefs.Prefs

interface PrefsFactory {
    fun createPrefs(): Prefs
}

// Note: crashes (at least on Android) if this extension is not used
private const val REQUIRED_PREFS_EXTENSION = "preferences_pb"

const val DATA_STORE_FILE_NAME = "prefs.$REQUIRED_PREFS_EXTENSION"
