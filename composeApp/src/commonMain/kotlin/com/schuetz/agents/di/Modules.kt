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
import com.schuetz.agents.client.HuggingFaceClient
import com.schuetz.agents.client.HuggingFaceClientImpl
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
import com.schuetz.agents.domain.AgentConnectionData
import com.schuetz.agents.domain.ConnectableProvider
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.LLMAgent
import com.schuetz.agents.domain.LLMAgentImpl
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.http.HttpClientFactory
import com.schuetz.agents.llm.DummyLLM
import com.schuetz.agents.llm.DummyModelProvider
import com.schuetz.agents.llm.ErrorLLM
import com.schuetz.agents.llm.HuggingFaceLLM
import com.schuetz.agents.llm.HuggingFaceModelProvider
import com.schuetz.agents.llm.OpenAILLM
import com.schuetz.agents.llm.OpenAIModelProvider
import com.schuetz.agents.prefs.Prefs
import com.schuetz.agents.spaces.SpacesRepo
import com.schuetz.agents.spaces.SpacesRepoImpl
import com.schuetz.agents.spaces.SpacesViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    // TODO
    // should be Dispatchers.IO, but for some reason not working on KMP
    // https://github.com/Kotlin/kotlinx.coroutines/issues/3205#issuecomment-2906627080
    single<CoroutineDispatcher> { Dispatchers.Default }

    factory<ChatRepo> { (llm: LLM) ->
        ChatRepoImpl(get(), llm, get(), get())
    }

    factory<LLMAgent> { LLMAgentImpl() }

    single<AgentsRepo> { AgentsRepoImpl(get(), get()) }
    single<SpacesRepo> { SpacesRepoImpl(get(), get()) }

    single<MessagesDao> { DbMessagesDao(get(), get()) }
    single<SpacesDao> { DbSpacesDao(get(), get(), get()) }
    single<AgentsDao> { DbAgentsDao(get(), get()) }

    single<MyDatabase> { MyDatabaseImpl(get()) }
    single<DatabaseFactory> { DatabaseFactoryImpl(get()) }

    single { HttpClientFactory.create(get()) }

    single<HuggingFaceClient> { HuggingFaceClientImpl(get()) }

    single<InitAppService> { InitAppServiceImpl(get(), get()) }

    single<Prefs> { get<PrefsFactory>().createPrefs() }

    single<AvatarUrlGenerator> { DiceBearClientImpl() }

    viewModel {
        SpacesViewModel(get(), get(), llmModelProvider = { data: ConnectableProvider ->
            when (data) {
                ConnectableProvider.HUGGING_FACE -> HuggingFaceModelProvider()
                ConnectableProvider.OPEN_AI -> OpenAIModelProvider()
                ConnectableProvider.DUMMY -> DummyModelProvider()
            }
        })
    }
    viewModel { (chatRepo: ChatRepo, space: SpaceData) -> ChatViewModel(chatRepo, get(), space) }

    single<LLM> { (connectionData: AgentConnectionData) ->
        when (connectionData) {
            is AgentConnectionData.HuggingFace -> HuggingFaceLLM(
                get<HuggingFaceClient>(),
                connectionData.model,
                connectionData.accessToken
            )

            is AgentConnectionData.OpenAI -> OpenAILLM(
                connectionData.model,
                connectionData.accessToken
            )

            AgentConnectionData.Dummy -> DummyLLM()
            // if there's no connection data, it means we're trying to chat with a non-connectable agent
            // (this is currently "me"), which is an error state
            // normally this shouldn't happen. The user flow shouldn't allow it.
            // it might be possible to improve design
            // maybe by requiring Space to reference actually connectable agents
            // or we could manage something more lenient like an echo llm for non-connectables
            AgentConnectionData.None -> ErrorLLM()
        }
    }
}
