package com.application.agent_ekr.examples

import com.application.agent_ekr.gigachat.GigaChatManager
import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import com.application.agent_ekr.models.common.GenerationParameters
import com.application.agent_ekr.tools.ToolRegistry
import com.application.agent_ekr.tools.examples.WeatherTool
import org.slf4j.LoggerFactory

/**
 * Example demonstrating the enhanced usage of the LLM manager with universal input
 */
class EnhancedUsageExample {
    private val logger = LoggerFactory.getLogger(EnhancedUsageExample::class.java)

    suspend fun demonstrateEnhancedUsage() {
        // Initialize the manager (using GigaChat in this example)
        val manager = GigaChatManager(
            clientSecret = "your-client-secret",
            logger = logger
        )

        // Initialize tool registry
        val toolRegistry = ToolRegistry()
        toolRegistry.register(WeatherTool())

        // Example 1: Backward compatible usage (single string)
        println("=== Example 1: Backward Compatible ===")
        manager.sendMessageStream("Hello, how are you?")
            .collect { chunk -> print(chunk) }
        println("\n")

        // Example 2: Enhanced usage with message history
        println("=== Example 2: Message History ===")
        val messages = listOf(
            ChatMessage.system("You are a helpful assistant that speaks like a pirate."),
            ChatMessage.user("Tell me about the weather")
        )
        manager.sendMessageStream(messages)
            .collect { chunk -> print(chunk) }
        println("\n")

        // Example 3: Full ChatInput with tools and parameters
        println("=== Example 3: Full ChatInput ===")
        val chatInput = ChatInput(
            messages = listOf(
                ChatMessage.system("You are a helpful assistant."),
                ChatMessage.user("What's the weather in Moscow?")
            ),
            tools = toolRegistry.getAvailableTools(),
            parameters = GenerationParameters(
                temperature = 0.7,
                maxTokens = 500
            ),
            model = "GIGA_CHAT_2"
        )
        manager.sendMessageStream(chatInput)
            .collect { chunk -> print(chunk) }
        println("\n")
    }
}