package com.schuetz.agents.di

import com.schuetz.agents.DataStoreFactory
import com.schuetz.agents.DesktopDataStoreFactory
import com.schuetz.agents.db.DesktopDriverFactory
import com.schuetz.agents.db.db.DriverFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DriverFactory> { DesktopDriverFactory() }
        single<HttpClientEngine> { OkHttp.create() }
        single<DataStoreFactory> { DesktopDataStoreFactory() }
    }
