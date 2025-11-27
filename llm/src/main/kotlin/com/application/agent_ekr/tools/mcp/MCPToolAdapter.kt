package com.application.agent_ekr.tools.mcp

import com.application.agent_ekr.models.common.ToolDefinition
import com.application.agent_ekr.tools.Tool

class MCPToolAdapter(
    private val registry: MCPToolRegistry,
    private val toolName: String
) : Tool {
    override val definition: ToolDefinition
        get() = registry.getToolDefinitions().find { it.name == toolName }
            ?: throw IllegalArgumentException("Tool $toolName not found")

    override suspend fun execute(arguments: String): String {
        return registry.executeTool(toolName, arguments)
    }
}
