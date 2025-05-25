package com.schuetz.agents.di

import com.schuetz.agents.agent.DummyLLM
import com.schuetz.agents.agent.LLM
import com.schuetz.agents.chat.ChatRepo
import com.schuetz.agents.chat.ChatRepoImpl
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.DbAgentsDao
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.db.DatabaseFactory
import com.schuetz.agents.db.DatabaseFactoryImpl
import com.schuetz.agents.db.DbDataSeeder
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.db.DbMessagesDao
import com.schuetz.agents.db.MyDatabase
import com.schuetz.agents.db.MyDatabaseImpl
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.LLMAgent
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<ChatRepo> { ChatRepoImpl(get()) }
    single<MessagesDao> { DbMessagesDao(get()) }
    single<AgentsDao> { DbAgentsDao(get()) }
    single<LLM> { DummyLLM() }
    single<MyDatabase> { MyDatabaseImpl(get()) }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }
    single<DataSeeder> { DbDataSeeder(get()) }
    viewModelOf(::ChatViewModel)
    viewModel { (agent: LLMAgent, me: AgentData) -> ChatViewModel(get(), agent, me) }
}
