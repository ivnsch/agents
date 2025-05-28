package com.schuetz.agents.di

import com.schuetz.agents.AvatarUrlGenerator
import com.schuetz.agents.InitAppService
import com.schuetz.agents.InitAppServiceImpl
import com.schuetz.agents.PrefsFactory
import com.schuetz.agents.agents.AgentsRepo
import com.schuetz.agents.agents.AgentsRepoImpl
import com.schuetz.agents.agents.AgentsViewModel
import com.schuetz.agents.chat.ChatRepo
import com.schuetz.agents.chat.ChatRepoImpl
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.db.db.DatabaseFactory
import com.schuetz.agents.db.db.DatabaseFactoryImpl
import com.schuetz.agents.db.db.MyDatabase
import com.schuetz.agents.db.db.MyDatabaseImpl
import com.schuetz.agents.db.mem.MemAgentsDao
import com.schuetz.agents.db.mem.MemMessagesDao
import com.schuetz.agents.dicebear.DiceBearClientImpl
import com.schuetz.agents.domain.HuggingFaceLLM
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.http.HttpClientFactory
import com.schuetz.agents.huggingface.HuggingFaceClient
import com.schuetz.agents.huggingface.HuggingFaceClientImpl
import com.schuetz.agents.huggingface.HuggingFaceTokenStore
import com.schuetz.agents.prefs.Prefs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    // TODO
    // should be Dispatchers.IO, but for some reason not working on KMP
    // https://github.com/Kotlin/kotlinx.coroutines/issues/3205#issuecomment-2906627080
    single<CoroutineDispatcher> { Dispatchers.Default }

    single<ChatRepo> { ChatRepoImpl(get(), get(), get()) }
    single<AgentsRepo> { AgentsRepoImpl(get(), get()) }
    single<MessagesDao> { MemMessagesDao() }
    single<AgentsDao> { MemAgentsDao() }
    single<LLM> { HuggingFaceLLM(get()) }
    single<MyDatabase> { MyDatabaseImpl(get()) }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }

    single { HttpClientFactory.create(get()) }

    single { HuggingFaceTokenStore(get(), get()) }
    single<HuggingFaceClient> { HuggingFaceClientImpl(get(), get()) }

    single<InitAppService> { InitAppServiceImpl(get(), get(), get()) }

    single<Prefs> { get<PrefsFactory>().createPrefs() }

    single<AvatarUrlGenerator> { DiceBearClientImpl() }

    viewModelOf(::ChatViewModel)
    viewModelOf(::AgentsViewModel)

    viewModel { (agent: LLMAgent) -> ChatViewModel(get(), agent, get()) }
}
