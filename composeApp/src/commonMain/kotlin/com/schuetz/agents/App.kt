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
import com.schuetz.agents.chat.ChatViewModel
import com.schuetz.agents.domain.SpaceData
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
                    ChatNavScreen(args.toSpace())
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
    val viewModel = koinViewModel<ChatViewModel>(parameters = {
        parametersOf(space)
    })
    Chat(viewModel)
}

@Composable
fun setTitle(titleState: MutableState<String>, title: String) {
    LaunchedEffect(title) {
        titleState.value = title
    }
}
