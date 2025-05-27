package com.schuetz.agents.di

import com.schuetz.agents.DataStoreFactory
import com.schuetz.agents.IOSDataStoreFactory
import com.schuetz.agents.db.IOSDriverFactory
import com.schuetz.agents.db.db.DriverFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DriverFactory> { IOSDriverFactory() }
        single<HttpClientEngine> { Darwin.create() }
        single<DataStoreFactory> { IOSDataStoreFactory() }
    }
