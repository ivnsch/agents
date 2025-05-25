package com.schuetz.agents.db

import app.cash.sqldelight.db.SqlDriver
import com.schuetz.agents.db.db.DriverFactory

class WebDriverFactory : DriverFactory {
    override fun createDriver(): SqlDriver =
        TODO()
}
