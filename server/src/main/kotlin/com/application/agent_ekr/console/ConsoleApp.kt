package com.application.agent_ekr.console

import com.application.agent_ekr.Env
import com.application.agent_ekr.Utils.runCatchingSuspend
import com.application.agent_ekr.lmm.managers.GigaChatManager
import io.ktor.util.logging.error
import org.slf4j.Logger

class ConsoleApp(
    val logger: Logger,
) {

    val gigaChatApi = GigaChatManager(
        clientSecret = Env.GIGACHAT_TOKEN,
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
        println("Ответ: \n")
        runCatchingSuspend {
            gigaChatApi.sendMessageStream(text).collect {
                print(it)
            }
        }.onFailure {
            logger.error(it)
        }

    }

}