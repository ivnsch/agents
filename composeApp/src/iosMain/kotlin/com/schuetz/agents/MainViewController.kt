package com.schuetz.agents

import androidx.compose.ui.window.ComposeUIViewController
import com.schuetz.agents.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
}) {
    App()
}
