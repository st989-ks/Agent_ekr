package com.application.agent_ekr

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Agent_ekr",
    ) {
        App()
    }
}