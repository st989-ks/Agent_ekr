package com.application.agent_ekr.tools

import com.application.agent_ekr.models.common.ToolCall
import com.application.agent_ekr.models.common.ToolDefinition
import com.application.agent_ekr.persistence.ToolResult

/**
 * Tool registry for managing available tools
 */
class ToolRegistry {
    private val tools = mutableMapOf<String, Tool>()

    fun register(tool: Tool) {
        tools[tool.definition.name] = tool
    }

    fun getTool(name: String): Tool? = tools[name]

    fun getAvailableTools(): List<ToolDefinition> = tools.values.map { it.definition }

    // TODO: Implement robust tool execution with comprehensive error handling
    suspend fun executeToolCall(toolCall: ToolCall): ToolResult {
        val tool = getTool(toolCall.function.name)
            ?: return ToolResult(
                toolCallId = toolCall.id,
                toolName = toolCall.function.name,
                result = "",
                success = false,
                error = "Tool not found: ${toolCall.function.name}"
            )

        return try {
            val result = tool.execute(toolCall.function.arguments)
            ToolResult(
                toolCallId = toolCall.id,
                toolName = toolCall.function.name,
                result = result,
                success = true
            )
        } catch (e: Exception) {
            ToolResult(
                toolCallId = toolCall.id,
                toolName = toolCall.function.name,
                result = "",
                success = false,
                error = "Execution failed: ${e.message}"
            )
        }
    }
}