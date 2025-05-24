package com.schuetz.agents.di

import com.schuetz.agents.agent.DummyLLM
import com.schuetz.agents.agent.LLM
import com.schuetz.agents.chat.ChatRepo
import com.schuetz.agents.chat.ChatRepoImpl
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.db.DatabaseFactory
import com.schuetz.agents.db.DatabaseFactoryImpl
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.db.MessagesDaoImpl
import com.schuetz.agents.db.MyDatabase
import com.schuetz.agents.db.MyDatabaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<ChatRepo> { ChatRepoImpl(get()) }
    single<LLM> { DummyLLM() }
    single<MessagesDao> { MessagesDaoImpl(get()) }
    single<MyDatabase> { MyDatabaseImpl(get()) }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }
    viewModelOf(::ChatViewModel)
}
