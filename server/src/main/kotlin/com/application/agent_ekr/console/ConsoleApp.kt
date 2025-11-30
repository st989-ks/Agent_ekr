package com.application.agent_ekr.console

import com.application.agent_ekr.Env
import com.application.agent_ekr.Utils.runCatchingSuspend
import com.application.agent_ekr.gigachat.GigaChatManager
import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import com.application.agent_ekr.tools.mcp.MCPToolRegistry
import kotlinx.serialization.json.Json
import org.slf4j.Logger

class ConsoleApp(
    val logger: Logger,
    val config: ConsoleUIConfig = ConsoleUIConfig.development()
) {
    val gigaChatApi = GigaChatManager(
        clientSecret = Env.GIGACHAT_TOKEN,
        logger = logger
    )
    private val mcpToolRegistry = MCPToolRegistry()
    private val commandHandler = ConsoleCommandHandler(
        logger = logger,
        config = config,
        mcpToolRegistry = mcpToolRegistry,
        availableTools = mutableListOf()
    )

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
                handleChatCalculator(input)
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

        userMessageDebugMode(text)

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

    private suspend fun handleChatCalculator(text: String) {

        userMessageDebugMode(text)

        val result = commandHandler.handleCommand("/addtool calculator")
        println(result)

        println()
        println(ConsoleStyler.messageBlock("Request", text))

        // Create initial chat input with tools
        val messages = mutableListOf<ChatMessage>(ChatMessage.user(text))

        // Chat loop to handle tool calls
        var maxIterations = 10
        while (maxIterations-- > 0) {
            val chatInput = ChatInput(
                messages = messages,
                tools = commandHandler.availableTools.ifEmpty { null }
            )

            // Get the response from the LLM
            val response = StringBuilder()
            runCatchingSuspend {
                gigaChatApi.sendMessageStream(chatInput).collect {
                    response.append(it)
                    print(it)
                }
            }.onFailure {
                logger.error("Error in handleChatCalculator", it)
                println(ConsoleStyler.error("Error processing message: ${it.message}"))
                return
            }

            val assistantMessage = ChatMessage.assistant(
                response.toString(),
                null // We'll handle tool calls separately
            )
            messages.add(assistantMessage)

            // Check if the response contains tool calls
            // For now, we'll parse the response to see if it wants to use tools
            // In a real implementation, we'd check for actual tool call objects
            val responseText = response.toString()

            // If the response doesn't indicate tool usage, we're done
            if (!responseText.contains("tool") && !responseText.contains("calculate") &&
                !responseText.contains("add") && !responseText.contains("subtract") &&
                !responseText.contains("multiply") && !responseText.contains("divide")
            ) {
                break
            }

            // For now, we'll implement a simple approach to detect and execute calculator tools
            // In a production system, this would use proper tool call parsing
            val toolCalls = parseToolCallsFromResponse(responseText)
            if (toolCalls.isEmpty()) {
                break
            }

            // Execute tool calls and add results to messages
            for (toolCall in toolCalls) {
                val result = executeToolCall(toolCall)
                val toolMessage = ChatMessage.tool(
                    content = result,
                    toolCallId = toolCall.id,
                    name = toolCall.name
                )
                messages.add(toolMessage)
                println("\n${ConsoleStyler.info("Tool Result:")} $result")
            }
        }

        println()
        println(ConsoleStyler.separator())
    }

    private fun parseToolCallsFromResponse(response: String): List<ToolCall> {
        val toolCalls = mutableListOf<ToolCall>()

        // Simple parsing for demonstration - in reality, this would come from the LLM's structured response
        // Check if the response mentions using calculator tools
        if (response.contains("add") || response.contains("subtract") ||
            response.contains("multiply") || response.contains("divide")
        ) {

            // Extract numbers from the response
            val numbers = Regex("\\d+").findAll(response).map { it.value.toDouble() }.toList()
            if (numbers.size >= 2) {
                when {
                    response.contains("add") -> {
                        toolCalls.add(
                            ToolCall(
                                id = "call_${System.currentTimeMillis()}",
                                name = "add",
                                arguments = mapOf("a" to numbers[0], "b" to numbers[1])
                            )
                        )
                    }

                    response.contains("subtract") -> {
                        toolCalls.add(
                            ToolCall(
                                id = "call_${System.currentTimeMillis()}",
                                name = "subtract",
                                arguments = mapOf("a" to numbers[0], "b" to numbers[1])
                            )
                        )
                    }

                    response.contains("multiply") -> {
                        toolCalls.add(
                            ToolCall(
                                id = "call_${System.currentTimeMillis()}",
                                name = "multiply",
                                arguments = mapOf("a" to numbers[0], "b" to numbers[1])
                            )
                        )
                    }

                    response.contains("divide") -> {
                        toolCalls.add(
                            ToolCall(
                                id = "call_${System.currentTimeMillis()}",
                                name = "divide",
                                arguments = mapOf("a" to numbers[0], "b" to numbers[1])
                            )
                        )
                    }
                }
            }
        }
        return toolCalls
    }

    private suspend fun executeToolCall(toolCall: ToolCall): String {
        return try {
            val argumentsJson = Json.encodeToString(
                toolCall.arguments
            )
            commandHandler.mcpToolRegistry.executeTool(toolCall.name, argumentsJson)
        } catch (e: Exception) {
            logger.error("Error executing tool: ${toolCall.name}", e)
            "Error: ${e.message}"
        }
    }

    private fun userMessageDebugMode(text: String) {
        // Log message in debug mode
        if (config.debugMode) {
            logger.debug("User message: $text")
        }
    }

    // Helper data class for tool calls
    private data class ToolCall(
        val id: String,
        val name: String,
        val arguments: Map<String, Any>
    )
}
