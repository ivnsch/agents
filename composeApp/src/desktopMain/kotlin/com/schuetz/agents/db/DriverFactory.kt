package com.schuetz.agents.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.schuetz.agents.Database
import java.util.Properties

class DesktopDriverFactory : DriverFactory {
    override fun createDriver(): SqlDriver =
        JdbcSqliteDriver("jdbc:sqlite:db.db", Properties(), Database.Schema)
}
