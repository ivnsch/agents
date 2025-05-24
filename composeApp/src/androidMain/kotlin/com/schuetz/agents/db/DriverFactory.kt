package com.schuetz.agents.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.schuetz.agents.Database

class AndroidDriverFactory(private val context: Context) : DriverFactory {
    override fun createDriver(): SqlDriver =
        AndroidSqliteDriver(Database.Schema, context, "db.db")
}
