package com.schuetz.agents.di

import com.schuetz.agents.db.db.DriverFactory
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.db.WebDriverFactory
import com.schuetz.agents.db.WebMessagesDao
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<MessagesDao> { WebMessagesDao() }
        single<DriverFactory> { WebDriverFactory() }
    }
