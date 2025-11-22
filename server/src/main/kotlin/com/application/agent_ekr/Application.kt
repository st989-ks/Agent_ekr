package com.application.agent_ekr

import com.application.agent_ekr.console.ConsoleApp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun main() {
    coroutineScope {
        val asyncConsole = async { ConsoleApp().start() }
//        val asyncBackend = async { BackendApp().start() }
        awaitAll(asyncConsole, /* asyncBackend*/)
    }
}
