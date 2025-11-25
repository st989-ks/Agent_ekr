package com.application.agent_ekr.console

import com.application.agent_ekr.Env
import com.application.agent_ekr.Utils.runCatchingSuspend
import com.application.agent_ekr.gigachat.GigaChatManager
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
        println(ConsoleStyler.success("Console ready. Type /exit to quit."))

        while (true) {
            println(ConsoleStyler.separator())
            print("> ")
            val input = readlnOrNull() ?: continue

            if (input == "/exit") {
                println(ConsoleStyler.info("Exiting console..."))
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
        println(ConsoleStyler.info("Команда: $cmd"))
    }

    private suspend fun handleChat(text: String) {
        println(ConsoleStyler.aiMessage("Ответ: \n"))
        runCatchingSuspend {
            gigaChatApi.sendMessageStream(text).collect {
                print(it)
            }
            println() // Add a newline after the response is complete
        }.onFailure {
            logger.error(it)
            println(ConsoleStyler.error("Error: ${it.message}"))
        }
    }

}
