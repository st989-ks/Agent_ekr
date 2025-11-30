package com.application.agent_ekr.tools.mcp

import com.application.agent_ekr.models.common.ToolDefinition
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequestParams
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import javax.swing.UIManager.put
import kotlin.time.Duration.Companion.seconds

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
            // Add timeout to prevent indefinite blocking
            try {
                withTimeout(10.seconds) {
                    client.connect(mcpTransport)
                }
                isConnected = true
            } catch (e: TimeoutCancellationException) {
                throw RuntimeException("MCP connection timeout: Server did not respond within 10 seconds")
            } catch (e: Exception) {
                // Add more detailed error information
                val errorMessage = when {
                    e.message?.contains("Connection closed") == true -> 
                        "MCP connection was closed by the server. The server may not be implementing the MCP protocol correctly."
                    else -> "Failed to connect to MCP server: ${e.message}"
                }
                throw RuntimeException(errorMessage, e)
            }
        }
    }

    suspend fun getTools(): List<ToolDefinition> {
        connect()
        // Add timeout for listTools as well
        try {
            val toolsResponse = withTimeout(5.seconds) {
                client.listTools()
            }
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
        } catch (e: TimeoutCancellationException) {
            throw RuntimeException("getTools timeout: Server did not respond within 5 seconds")
        } catch (e: Exception) {
            throw RuntimeException("Failed to get tools from MCP server: ${e.message}", e)
        }
    }

    suspend fun callTool(name: String, arguments: Map<String, Any>): String {
        connect()
        try {
            val result = withTimeout(10.seconds) {
                client.callTool(
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
            }
            return result.content.joinToString("\n") { it.toString() }
        } catch (e: TimeoutCancellationException) {
            throw RuntimeException("callTool timeout: Server did not respond within 10 seconds")
        } catch (e: Exception) {
            throw RuntimeException("Failed to call tool '$name': ${e.message}", e)
        }
    }

    override fun close() {
        runBlocking {
            try {
                client.close()
            } catch (e: Exception) {
                // Log but don't throw during close
                System.err.println("Error closing MCP client: ${e.message}")
            }
            isConnected = false
        }
    }
}
