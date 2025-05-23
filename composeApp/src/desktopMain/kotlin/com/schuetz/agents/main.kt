package com.schuetz.agents

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.schuetz.agents.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "agents",
    ) {
        App()
    }
}
