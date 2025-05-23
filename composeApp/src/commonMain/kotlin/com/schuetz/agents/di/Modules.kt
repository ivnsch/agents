package com.schuetz.agents.di

import com.schuetz.agents.agent.DummyLLM
import com.schuetz.agents.agent.LLM
import com.schuetz.agents.chat.ChatRepo
import com.schuetz.agents.chat.ChatRepoImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.schuetz.agents.chat.ChatViewModel

val sharedModule = module {
    single<ChatRepo> { ChatRepoImpl() }
    single<LLM> { DummyLLM() }
    viewModelOf(::ChatViewModel)
}
