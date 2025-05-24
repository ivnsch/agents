package com.schuetz.agents.db

import app.cash.sqldelight.db.SqlDriver
import com.schuetz.agents.Database

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

interface DatabaseFactory {
    fun create(): Database
}

class DatabaseFactoryImpl(
    private val driverFactory: DriverFactory
) : DatabaseFactory {

    override fun create(): Database {
        val driver = driverFactory.createDriver()
        val database = Database(driver)
        return database
    }
}
