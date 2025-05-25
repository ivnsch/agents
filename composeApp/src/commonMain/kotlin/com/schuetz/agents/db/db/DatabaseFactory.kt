package com.schuetz.agents.db.db

import com.schuetz.agents.Database

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
