package com.schuetz.agents

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.schuetz.agents.agents.Agents
import com.schuetz.agents.agents.AgentsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val viewModel = koinViewModel<AgentsViewModel>()
    MaterialTheme {
        Agents(viewModel)
    }
}
