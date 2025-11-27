package com.application.agent_ekr.tools

import com.application.agent_ekr.tools.mcp.MCPToolRegistry
import com.application.agent_ekr.models.common.ToolCall
import com.application.agent_ekr.models.common.ToolDefinition
import com.application.agent_ekr.persistence.ToolResult

class ToolRegistry {
    private val tools = mutableMapOf<String, Tool>()
    private val mcpRegistry = MCPToolRegistry()

    fun register(tool: Tool) {
        tools[tool.definition.name] = tool
    }

    fun registerMCPTools(mcpRegistry: MCPToolRegistry) {
        // MCP tools are handled separately
    }

    fun getTool(name: String): Tool? = tools[name] 

    fun getAvailableTools(): List<ToolDefinition> = 
        tools.values.map { it.definition } + mcpRegistry.getToolDefinitions()

    suspend fun executeToolCall(toolCall: ToolCall): ToolResult {
        val toolName = toolCall.function.name
        
        // Check if it's a regular tool
        val tool = getTool(toolName)
        if (tool != null) {
            return try {
                val result = tool.execute(toolCall.function.arguments)
                ToolResult(
                    toolCallId = toolCall.id,
                    toolName = toolName,
                    result = result,
                    success = true
                )
            } catch (e: Exception) {
                ToolResult(
                    toolCallId = toolCall.id,
                    toolName = toolName,
                    result = "",
                    success = false,
                    error = "Execution failed: ${e.message}"
                )
            }
        }
        
        // Check if it's an MCP tool
        return try {
            val result = mcpRegistry.executeTool(toolName, toolCall.function.arguments)
            ToolResult(
                toolCallId = toolCall.id,
                toolName = toolName,
                result = result,
                success = true
            )
        } catch (e: Exception) {
            ToolResult(
                toolCallId = toolCall.id,
                toolName = toolName,
                result = "",
                success = false,
                error = "MCP tool execution failed: ${e.message}"
            )
        }
    }
}
