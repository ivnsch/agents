package com.schuetz.agents.di

import com.schuetz.agents.db.DriverFactory
import com.schuetz.agents.db.IOSDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DriverFactory> { IOSDriverFactory() }
    }
