package com.application.agent_ekr.console

import com.application.agent_ekr.Env
import com.application.agent_ekr.Utils.runCatchingSuspend
import com.application.agent_ekr.gigachat.GigaChatManager
import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import com.application.agent_ekr.models.common.ToolCall
import com.application.agent_ekr.models.common.ToolDefinition
import kotlinx.serialization.json.Json
import org.slf4j.Logger

class ConsoleApp(
    val logger: Logger,
) {

    val gigaChatApi = GigaChatManager(
        clientSecret = Env.GIGACHAT_TOKEN,
        logger = logger
    )

    private val toolRegistry = ConsoleToolRegistry(logger, ConsoleUIConfig.development())
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Register a tool to be available for the LLM to use
     */
    fun registerTool(tool: ConsoleTool) {
        toolRegistry.registerTool(tool)
    }

    /**
     * Get all registered tool definitions
     */
    private fun getToolDefinitions(): List<ToolDefinition> {
        return toolRegistry.getAllToolDefinitions()
    }

    suspend fun start() {
        println(ConsoleStyler.success("Console ready. Type /exit to quit."))
        println(ConsoleStyler.info("Registered ${tools.size} tools"))

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
        when {
            cmd == "/tools" -> listTools()
            else -> println(ConsoleStyler.info("Команда: $cmd"))
        }
    }

    private fun listTools() {
        val toolsList = toolRegistry.listTools()
        println(
            ConsoleStyler.messageBlock(
                "Available Tools",
                toolsList
            )
        )
    }

    private suspend fun handleChat(text: String) {
        val messages = mutableListOf<ChatMessage.User>(ChatMessage.User(text))

        // Continue conversation until no more tool calls are needed
        var continueProcessing = true
        var iterationCount = 0
        val maxIterations = 5

        while (continueProcessing && iterationCount < maxIterations) {
            iterationCount++

            val chatInput = ChatInput(
                messages = messages.toList(),
                tools = if (tools.isNotEmpty()) getToolDefinitions() else null
            )

            val response = runCatchingSuspend {
                gigaChatApi.sendMessageStream(chatInput).collect { chunk ->
                    print(chunk)
                }
                println() // Add a newline after the response is complete
            }

            // In a real implementation, we would parse the response to check for tool calls
            // For now, we'll assume the response is complete and check if we need to process tool calls
            // This is a simplified implementation - in practice, you'd need to parse the actual response

            // For demonstration, we'll break after one iteration
            continueProcessing = false
        }

        if (iterationCount >= maxIterations) {
            println(ConsoleStyler.warning("Reached maximum tool call iterations"))
        }
    }

    /**
     * Execute a tool call and return the result
     */
    private suspend fun executeToolCall(toolCall: ToolCall): String {
        val result = toolRegistry.executeTool(toolCall.function.name, toolCall.function.arguments)
        return when (result) {
            is ToolExecutionResult.Success -> result.result
            is ToolExecutionResult.NotFound -> "Tool not found: ${result.toolName}"
            is ToolExecutionResult.Error -> "Error executing ${result.toolName}: ${result.error}"
        }
    }
}
