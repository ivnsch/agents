package com.schuetz.agents.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.schuetz.agents.Database
import java.util.Properties

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver =
            JdbcSqliteDriver("jdbc:sqlite:db.db", Properties(), Database.Schema)
        return driver
    }
}
