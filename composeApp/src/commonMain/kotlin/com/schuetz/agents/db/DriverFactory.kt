package com.schuetz.agents.db

import app.cash.sqldelight.db.SqlDriver
import com.schuetz.agents.Database

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    val database = Database(driver)
    return database
}
