package com.schuetz.agents.di

import com.schuetz.agents.PrefsFactory
import com.schuetz.agents.WebPrefsFactory
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.db.SpacesDao
import com.schuetz.agents.db.WebDriverFactory
import com.schuetz.agents.db.db.DriverFactory
import com.schuetz.agents.db.mem.MemAgentsDao
import com.schuetz.agents.db.mem.MemMessagesDao
import com.schuetz.agents.db.mem.MemSpacesDao
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.JsClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<MessagesDao> { MemMessagesDao() }
        single<AgentsDao> { MemAgentsDao() }
        single<SpacesDao> { MemSpacesDao(get()) }
        single<DriverFactory> { WebDriverFactory() }
        single<HttpClientEngine> { JsClient().create() }
        single<PrefsFactory> { WebPrefsFactory() }
    }
