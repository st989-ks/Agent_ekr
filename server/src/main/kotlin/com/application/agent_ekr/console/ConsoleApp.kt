package com.application.agent_ekr.console

import com.application.agent_ekr.Env
import com.application.agent_ekr.Utils.runCatchingSuspend
import com.application.agent_ekr.gigachat.GigaChatManager
import com.application.agent_ekr.tools.ConsoleTool
import org.slf4j.Logger

class ConsoleApp(
    val logger: Logger,
    val config: ConsoleUIConfig = ConsoleUIConfig.development()
) {
    val gigaChatApi = GigaChatManager(
        clientSecret = Env.GIGACHAT_TOKEN,
        logger = logger
    )
    private val commandHandler = ConsoleCommandHandler(logger, config)

    /**
     * Register a tool to be available for execution
     *
     * @param tool The ConsoleTool implementation to register
     */
    fun registerTool(tool: ConsoleTool) {
    }

    suspend fun start() {
        printWelcome()

        while (true) {
            printPrompt()
            val input = readlnOrNull() ?: continue

            // Skip empty input
            if (input.isBlank()) {
                continue
            }

            if (input.startsWith("/")) {
                when (val result = commandHandler.handleCommand(input)) {
                    is CommandResult.Output -> println(result.message)
                    is CommandResult.Success -> {
                        // Command succeeded silently, do nothing
                    }

                    is CommandResult.Exit -> {
                        println("Goodbye!")
                        return
                    }

                    is CommandResult.Error -> println(ConsoleStyler.error(result.message))
                    is CommandResult.Unknown -> println(ConsoleStyler.error("Unknown command: '${result.command}'. Type /help"))
                }
            } else {
                handleChat(input)
            }
        }
    }

    /**
     * Print welcome message with application information
     */
    private fun printWelcome() {
        println(ConsoleStyler.separator())
        println(ConsoleStyler.success("🤖 Console Agent"))
        println(ConsoleStyler.info("Type /help for commands"))
        println(ConsoleStyler.info("Type /exit to quit"))
        println(ConsoleStyler.separator())
    }

    /**
     * Print the command prompt
     */
    private fun printPrompt() {
        print(ConsoleStyler.userMessage(""))
        print(" > ")
    }

    private suspend fun handleChat(text: String) {
        // Log message in debug mode
        if (config.debugMode) {
            logger.debug("User message: $text")
        }

        println()
        println(ConsoleStyler.messageBlock("Request", text))

        runCatchingSuspend {
            gigaChatApi.sendMessageStream(text).collect {
                print(it)
            }
        }.onFailure {
            logger.error("Error in handleChat", it)
            println(ConsoleStyler.error("Error processing message: ${it.message}"))
        }

        println()
        println(ConsoleStyler.separator())
    }
}
