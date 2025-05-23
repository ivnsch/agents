package com.schuetz.agents

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.schuetz.agents.chat.Chat
import com.schuetz.agents.chat.exampleState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Chat(state = exampleState)
    }
}
