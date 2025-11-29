package com.application.agent_ekr.tools.mcp

import com.application.agent_ekr.models.common.ToolDefinition
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequestParams
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import javax.swing.UIManager.put

class UniversalMCPClient(
    private val transport: MCPTransport,
    clientName: String = "agent-ekr-mcp-client",
    clientVersion: String = "1.0.0"
): AutoCloseable  {
    private val client = Client(
        clientInfo = Implementation(name = clientName, version = clientVersion)
    )

    private var isConnected = false

    suspend fun connect() {
        if (!isConnected) {
            val mcpTransport = transport.createTransport()
            // TODO("The flow is blocked here, it is necessary to provide work on parallel flow")
            client.connect(mcpTransport)
            isConnected = true
        }
    }

    suspend fun getTools(): List<ToolDefinition> {
        connect()
        val toolsResponse = client.listTools()
        return toolsResponse.tools.map { tool ->
            ToolDefinition(
                name = tool.name,
                description = tool.description ?: "",
                parameters = tool.inputSchema.properties ?: buildJsonObject {
                    put("type", "object")
                    put("properties", buildJsonObject {})
                    put("required", Json.decodeFromString<List<String>>("[]"))
                }
            )
        }
    }

    suspend fun callTool(name: String, arguments: Map<String, Any>): String {
        connect()
        val result = client.callTool(
            CallToolRequest(
                params = CallToolRequestParams(
                    name = name,
                    arguments = buildJsonObject {
                        arguments.forEach { (key, value) ->
                            put(key, Json.encodeToJsonElement(value))
                        }
                    }
                )
            )
        )
        return result.content.joinToString("\n") { it.toString() }
    }

    override fun close() {
        runBlocking {
            client.close()
            isConnected = false
        }
    }
}
