package com.schuetz.agents.di

import com.schuetz.agents.chat.ChatRepo
import com.schuetz.agents.chat.ChatRepoImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.schuetz.agents.chat.ChatViewModel

val sharedModule = module {
    single<ChatRepo> { ChatRepoImpl() }
    viewModelOf(::ChatViewModel)
}
