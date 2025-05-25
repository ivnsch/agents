package com.schuetz.agents

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.schuetz.agents.agent.LLM
import com.schuetz.agents.chat.Chat
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.domain.LLMAgent
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Preview
fun App() {
    val dataSeeder = koinInject<DataSeeder>()
    val llm = koinInject<LLM>()

    // TODO
    // TEMPORARY IMPLEMENTATION
    // this shouldn't be executed on main thread, for now ok since this will be rewritten
    val viewModel = koinViewModel<ChatViewModel>(parameters = {
        val seed = dataSeeder.seed()
        val llmDummyAgent = LLMAgent(seed.agents.dummy, llm)
        parametersOf(
            llmDummyAgent,
            seed.agents.me
        )
    })
    MaterialTheme {
        Chat(viewModel)
    }
}

