package com.application.agent_ekr.tools.mcp

import com.application.agent_ekr.models.common.ToolDefinition
import kotlinx.serialization.json.Json

class MCPToolRegistry {
    private val clients = mutableMapOf<String, UniversalMCPClient>()
    private val tools = mutableMapOf<String, MCPTool>()

    fun registerClient(name: String, client: UniversalMCPClient) {
        clients[name] = client
    }

    suspend fun discoverTools(clientName: String) {
        val client = clients[clientName] ?: return
        val toolDefs = client.getTools()
        toolDefs.forEach { toolDef ->
            tools[toolDef.name] = MCPTool(clientName, toolDef, client)
        }
    }

    fun getToolDefinitions(): List<ToolDefinition> = tools.values.map { it.definition }

    suspend fun executeTool(name: String, arguments: String): String {
        val tool = tools[name] ?: return "Tool not found: $name"
        val argsMap = Json.decodeFromString<Map<String, Any>>(arguments)
        return tool.client.callTool(tool.definition.name, argsMap)
    }

    data class MCPTool(
        val clientName: String,
        val definition: ToolDefinition,
        val client: UniversalMCPClient
    )
}
