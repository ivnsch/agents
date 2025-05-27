package com.schuetz.agents

import com.schuetz.agents.prefs.Prefs

class WebPrefsFactory : PrefsFactory {
    override fun createPrefs(): Prefs = WebPrefs()
}
