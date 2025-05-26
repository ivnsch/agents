package com.schuetz.agents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.schuetz.agents.NavConversions.toAgentData
import com.schuetz.agents.NavConversions.toChatNav
import com.schuetz.agents.agents.Agents
import com.schuetz.agents.agents.AgentsViewModel
import com.schuetz.agents.chat.Chat
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.db.DataSeeder
import com.schuetz.agents.db.SeededData
import com.schuetz.agents.domain.AgentData
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
    val titleState = remember { mutableStateOf("") }

    MaterialTheme {
        Column {
            TopBar(navController, titleState.value)
            NavHost(
                navController = navController,
                startDestination = AgentsNav,
                modifier = Modifier.fillMaxSize(),
            ) {
                composable<AgentsNav> {
                    setTitle(titleState, "Agents")
                    AgentsScreen(navController)
                }
                composable<ChatNav> { backStackEntry ->
                    val args = backStackEntry.toRoute<ChatNav>()
                    setTitle(titleState, "Chat with ${args.name}")
                    ChatNavScreen(args.toAgentData())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, title: String) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canPop = remember(backStackEntry) {
        navController.previousBackStackEntry != null
    }
    return TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (canPop) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun AgentsScreen(navController: NavHostController) {
    val viewModel = koinViewModel<AgentsViewModel>()
    return Agents(viewModel, onAgentSelected = { agent ->
        navController.navigate(route = toChatNav(agent))
    })
}

@Composable
fun ChatNavScreen(agent: AgentData) {
    val dataSeeder = koinInject<DataSeeder>()
    val llm = koinInject<LLM>()

    val seedState = produceState<SeededData?>(initialValue = null) {
        value = dataSeeder.seed()
    }
    val seed = seedState.value

    if (seed == null) {
        // this could be a progress indicator, but might cause flickering
        // since it's a very short operation
        Box {}
    } else {
        val viewModel = koinViewModel<ChatViewModel>(parameters = {
            parametersOf(
                LLMAgent(agent, llm),
                seed.agents.me
            )
        })
        Chat(viewModel)
    }
}

@Composable
fun setTitle(titleState: MutableState<String>, title: String) {
    LaunchedEffect(title) {
        titleState.value = title
    }
}
