package com.schuetz.agents.di

import com.schuetz.agents.AvatarUrlGenerator
import com.schuetz.agents.InitAppService
import com.schuetz.agents.InitAppServiceImpl
import com.schuetz.agents.PrefsFactory
import com.schuetz.agents.agents.AgentsRepo
import com.schuetz.agents.agents.AgentsRepoImpl
import com.schuetz.agents.chat.ChatRepo
import com.schuetz.agents.chat.ChatRepoImpl
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.db.AgentsDao
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.db.SpacesDao
import com.schuetz.agents.db.db.DatabaseFactory
import com.schuetz.agents.db.db.DatabaseFactoryImpl
import com.schuetz.agents.db.db.DbAgentsDao
import com.schuetz.agents.db.db.DbMessagesDao
import com.schuetz.agents.db.db.DbSpacesDao
import com.schuetz.agents.db.db.MyDatabase
import com.schuetz.agents.db.db.MyDatabaseImpl
import com.schuetz.agents.dicebear.DiceBearClientImpl
import com.schuetz.agents.domain.HuggingFaceLLM
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.http.HttpClientFactory
import com.schuetz.agents.huggingface.HuggingFaceClient
import com.schuetz.agents.huggingface.HuggingFaceClientImpl
import com.schuetz.agents.prefs.Prefs
import com.schuetz.agents.spaces.SpacesRepo
import com.schuetz.agents.spaces.SpacesRepoImpl
import com.schuetz.agents.spaces.SpacesViewModel
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

    factory<ChatRepo> { (llm: LLM) -> ChatRepoImpl(get(), llm, get()) }

    single<AgentsRepo> { AgentsRepoImpl(get(), get()) }
    single<SpacesRepo> { SpacesRepoImpl(get(), get()) }

    single<MessagesDao> { DbMessagesDao(get(), get()) }
    single<SpacesDao> { DbSpacesDao(get(), get(), get()) }
    single<AgentsDao> { DbAgentsDao(get(), get()) }

    single<LLM> { HuggingFaceLLM(get()) }
    single<MyDatabase> { MyDatabaseImpl(get()) }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }

    single { HttpClientFactory.create(get()) }

    single<HuggingFaceClient> { HuggingFaceClientImpl(get()) }

    single<InitAppService> { InitAppServiceImpl(get(), get()) }

    single<Prefs> { get<PrefsFactory>().createPrefs() }

    single<AvatarUrlGenerator> { DiceBearClientImpl() }

    viewModelOf(::SpacesViewModel)
    viewModel { (chatRepo: ChatRepo, space: SpaceData) -> ChatViewModel(chatRepo, get(), space) }
}
