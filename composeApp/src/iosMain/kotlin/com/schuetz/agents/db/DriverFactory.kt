package com.schuetz.agents.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.schuetz.agents.Database

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, "db.db")
    }
}
