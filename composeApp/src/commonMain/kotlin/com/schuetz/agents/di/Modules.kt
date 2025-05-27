package com.schuetz.agents.di

import com.schuetz.agents.agents.AgentsRepo
import com.schuetz.agents.agents.AgentsRepoImpl
import com.schuetz.agents.agents.AgentsViewModel
import com.schuetz.agents.chat.ChatRepo
import com.schuetz.agents.chat.ChatRepoImpl
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.db.db.DatabaseFactory
import com.schuetz.agents.db.db.DatabaseFactoryImpl
import com.schuetz.agents.db.db.MyDatabase
import com.schuetz.agents.db.db.MyDatabaseImpl
import com.schuetz.agents.db.mem.MemAgentsDao
import com.schuetz.agents.db.mem.MemDataSeeder
import com.schuetz.agents.db.mem.MemMessagesDao
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.HuggingFaceLLM
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.http.HttpClientFactory
import com.schuetz.agents.huggingface.HuggingFaceClient
import com.schuetz.agents.huggingface.HuggingFaceClientImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<ChatRepo> { ChatRepoImpl(get(), get()) }
    single<AgentsRepo> { AgentsRepoImpl(get()) }
    single<MessagesDao> { MemMessagesDao() }
    single<AgentsDao> { MemAgentsDao(get()) }
    single<DataSeeder> { MemDataSeeder() }
    single<LLM> { HuggingFaceLLM(get()) }
    single<MyDatabase> { MyDatabaseImpl(get()) }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }

    single { HttpClientFactory.create(get()) }
    single<HuggingFaceClient> { HuggingFaceClientImpl(get()) }

    viewModelOf(::ChatViewModel)
    viewModelOf(::AgentsViewModel)

    viewModel { (agent: LLMAgent, me: AgentData) -> ChatViewModel(get(), agent, me) }
}
