package com.application.agent_ekr.console

import com.application.agent_ekr.Env
import com.application.agent_ekr.lmm.managers.GigaChatManager
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.slf4j.Logger

class ConsoleApp(
    val logger: Logger,
) {

    val gigaChatApi = GigaChatManager(
        tokenGigaChat = Env.GIGACHAT_TOKEN,
        logger = logger
    )

    suspend fun start() {
        println("Console ready. Type /exit to quit.")

        while (true) {
            print("> ")
            val input = readlnOrNull() ?: continue

            if (input == "/exit") {
                println("Exiting console...")
                return
            }

            if (input.startsWith("/")) {
                handleCommand(input)
            } else {
                handleChat(input)
            }
        }
    }

    private fun handleCommand(cmd: String) {
        println("Команда: $cmd")
    }

    private suspend fun handleChat(text: String) {
        println("Запрос: $text")
        println("Ответ: \n")
        gigaChatApi.sendMessageStream(text).collect {
            print(it)
        }
    }

}