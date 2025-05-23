package com.schuetz.agents.di

import com.schuetz.agents.db.AndroidDriverFactory
import com.schuetz.agents.db.DriverFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DriverFactory> { AndroidDriverFactory(androidApplication()) }
    }
