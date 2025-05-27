package com.schuetz.agents.di

import com.schuetz.agents.AndroidPrefsFactory
import com.schuetz.agents.PrefsFactory
import com.schuetz.agents.db.AndroidDriverFactory
import com.schuetz.agents.db.db.DriverFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DriverFactory> { AndroidDriverFactory(androidApplication()) }
        single<HttpClientEngine> { OkHttp.create() }
        single<PrefsFactory> { AndroidPrefsFactory(androidApplication()) }
    }
