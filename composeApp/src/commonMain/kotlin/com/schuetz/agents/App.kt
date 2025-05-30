package com.schuetz.agents

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.schuetz.agents.NavConversions.toChatNav
import com.schuetz.agents.NavConversions.toSpace
import com.schuetz.agents.chat.Chat
import com.schuetz.agents.chat.ChatRepo
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.domain.AgentConnectionData
import com.schuetz.agents.llm.DummyLLM
import com.schuetz.agents.llm.ErrorLLM
import com.schuetz.agents.llm.HuggingFaceLLM
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.client.HuggingFaceClient
import com.schuetz.agents.spaces.Spaces
import com.schuetz.agents.spaces.SpacesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Preview
fun App() {
    InitAppServiceEffect()

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
                    val space = args.toSpace().getOrElse { error ->
                        // TODO error dialog
                        // note: can only happen due to programmatic error
                        println("Error converting args to space: $error")
                        return@composable
                    }
                    ChatNavScreen(space)
                }
            }
        }
    }
}

// Run long-running operations at app start (like loading a global preference)
// This approach:
// - keeps coroutines / dispatching logic out of core services
// - convenience: App() is the earliest common point for multiplatform
@Composable
inline fun InitAppServiceEffect() {
    val initAppService = koinInject<InitAppService>()
    LaunchedEffect(Unit) {
        // Dispatchers.IO currently not working
        // see https://github.com/Kotlin/kotlinx.coroutines/issues/3205#issuecomment-2906627080
        withContext(Dispatchers.Default) {
            initAppService.init()
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
    val viewModel = koinViewModel<SpacesViewModel>()
    return Spaces(viewModel, onSpaceSelected = { space ->
        navController.navigate(route = toChatNav(space))
    })
}

@Composable
fun ChatNavScreen(space: SpaceData) {
    val llm = when (space.agent.connectionData) {
        is AgentConnectionData.HuggingFace -> HuggingFaceLLM(
            koinInject<HuggingFaceClient>(),
            space.agent.connectionData.model,
            space.agent.connectionData.accessToken
        )

        is AgentConnectionData.Dummy -> DummyLLM()
        // if there's no connection data, it means we're trying to chat with a non-connectable agent
        // (this is currently "me"), which is an error state
        // normally this shouldn't happen. The user flow shouldn't allow it.
        // it might be possible to improve design
        // maybe by requiring Space to reference actually connectable agents
        // or we could manage something more lenient like an echo llm for non-connectables
        AgentConnectionData.None -> ErrorLLM()
    }

    val chatRepo = koinInject<ChatRepo> { parametersOf(llm) }

    val viewModel = koinViewModel<ChatViewModel>(parameters = {
        parametersOf(chatRepo, space)
    })
    Chat(viewModel)
}

@Composable
fun setTitle(titleState: MutableState<String>, title: String) {
    LaunchedEffect(title) {
        titleState.value = title
    }
}
