package com.schuetz.agents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.schuetz.agents.agents.Agents
import com.schuetz.agents.agents.AgentsViewModel
import com.schuetz.agents.chat.Chat
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.domain.LLM
import com.schuetz.agents.domain.LLMAgent
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()
    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = AgentsNav,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<AgentsNav> {
                val viewModel = koinViewModel<AgentsViewModel>()
                Agents(viewModel, onAgentSelected = {
                    navController.navigate(route = ChatNav)
                })
            }
            composable<ChatNav> {
                val dataSeeder = koinInject<DataSeeder>()
                val llm = koinInject<LLM>()

                val viewModel = koinViewModel<ChatViewModel>(parameters = {
                    val seed = dataSeeder.seed()
                    val llmDummyAgent = LLMAgent(seed.agents.dummy, llm)
                    parametersOf(
                        llmDummyAgent,
                        seed.agents.me
                    )
                })
                Chat(viewModel)

            }
        }
    }
}
