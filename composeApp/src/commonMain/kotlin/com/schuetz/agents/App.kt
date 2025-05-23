package com.schuetz.agents

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.schuetz.agents.chat.Chat
import com.schuetz.agents.chat.ChatViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
@Preview
fun App() {
    val viewModel: ChatViewModel = viewModel()
    MaterialTheme {
        Chat(viewModel)
    }
}
