package com.schuetz.agents

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.schuetz.agents.chat.Chat
import com.schuetz.agents.chat.ChatViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val viewModel = koinViewModel<ChatViewModel>()
    MaterialTheme {
        Chat(viewModel)
    }
}
