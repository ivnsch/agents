package com.schuetz.agents.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.schuetz.agents.Database
import com.schuetz.agents.db.db.DriverFactory

class IOSDriverFactory : DriverFactory {
    override fun createDriver(): SqlDriver =
        NativeSqliteDriver(Database.Schema, "db.db")
}