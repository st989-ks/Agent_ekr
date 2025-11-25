package com.application.agent_ekr.console.tools

import com.application.agent_ekr.console.ConsoleTool
import com.application.agent_ekr.models.common.ToolDefinition
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * ExampleConsoleTool demonstrates how to implement a console tool.
 * 
 * This tool echoes the input text with a prefix.
 */
class ExampleConsoleTool : ConsoleTool {
    override val definition = ToolDefinition(
        name = "echo",
        description = "Echoes the input text with a prefix",
        parameters = buildJsonObject {
            put("text", buildJsonObject {
                put("type", "string")
                put("description", "The text to echo")
            })
        }
    )

    override suspend fun execute(arguments: String): String {
        // Parse arguments and execute the tool
        // In a real implementation, you'd use a JSON parser
        // For now, we'll just return a simple response
        return "Echo: $arguments"
    }
}
