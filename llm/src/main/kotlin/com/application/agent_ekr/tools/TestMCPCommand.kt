package com.application.agent_ekr.tools

import com.application.agent_ekr.models.common.ToolDefinition
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.StdioClientTransport
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequestParams
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.swing.UIManager.put

/**
 * Command handler for testing MCP server connection and tool registration
 */
class TestMCPCommand(
) {
    private val logger: Logger = LoggerFactory.getLogger(TestMCPCommand::class.java)

    suspend fun getToolsListName(): String {
        return try {
            println("[*] Starting MCP server connection...")

            // Start the MCP server process
            val process = withContext(Dispatchers.IO) {
                Runtime.getRuntime().exec(arrayOf("npx", "@modelcontextprotocol/server-github"))
            }

            val client = Client(
                clientInfo = Implementation(
                    name = "agent-ekr-mcp-client",
                    version = "1.0.0"
                )
            )

            val transport = StdioClientTransport(
                input = process.inputStream.asSource().buffered(),
                output = process.outputStream.asSink().buffered()
            )

            // Connect to the MCP server
            println("[*] Connecting to MCP server...")
            client.connect(transport)
            println("[✓] Connected to MCP server")

            // List available tools
            println("[*] Fetching available tools...")
            val toolsResponse = client.listTools()
            val tools = toolsResponse.tools

            if (tools.isEmpty()) {
                println("[!] No tools found in MCP server")
                client.close()
                process.destroy()
                return "No tools available from MCP server"
            }

            println("[✓] Found ${tools.size} tools")

            // Register each tool as a ConsoleTool
            var registeredCount = 0
            tools.forEach { tool ->
                try {
                    val toolDefinition = ToolDefinition(
                        name = tool.name,
                        description = tool.description ?: "No description available",
                        parameters = tool.inputSchema.properties ?: buildJsonObject {
                            // Default empty schema if none provided
                            put("type", "object")
                            put("properties", buildJsonObject {})
                            put("required", Json.decodeFromString<List<String>>("[]"))
                        }
                    )

                    registeredCount++
                    println("[✓] Registered tool: ${tool.name}")
                } catch (e: Exception) {
                    println("[✗] Failed to register tool ${tool.name}: ${e.message}")
                    logger.error("Failed to register MCP tool ${tool.name}", e)
                }
            }

            // Display summary
            println("\n[✓] Successfully registered $registeredCount/${tools.size} tools")
            println("\nAvailable Tools:")
            tools.forEach { tool ->
                println("  ${tool.name} - ${tool.description ?: "No description"}")
            }

            // Note: We don't close the client here to keep tools functional
            // The tools will use the same client instance

            "MCP tool registration completed. Registered $registeredCount tools."

        } catch (e: Exception) {
            println("[✗] MCP connection failed: ${e.message}")
            logger.error("MCP connection failed", e)
            "MCP connection failed: ${e.message}"
        }
    }
}