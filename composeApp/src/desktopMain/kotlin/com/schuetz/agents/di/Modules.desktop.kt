package com.schuetz.agents.di

import com.schuetz.agents.db.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DriverFactory() }
    }
