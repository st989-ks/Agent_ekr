package com.application.agent_ekr

import com.application.agent_ekr.console.ConsoleApp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory

suspend fun main() {
    val logger = LoggerFactory.getLogger("agent_ekr")
    coroutineScope {
        val asyncConsole = async { ConsoleApp(logger).start() }
//        val asyncBackend = async { BackendApp(logger).start() }
        awaitAll(asyncConsole, /* asyncBackend*/)
    }
}
