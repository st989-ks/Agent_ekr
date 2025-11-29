package com.application.agent_ekr.calculator

import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import io.modelcontextprotocol.kotlin.sdk.types.ToolSchema
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Calculator MCP Server - Provides basic arithmetic operations
 */
class CalculatorMCPServer {
    private val server = Server(
        serverInfo = Implementation(
            name = "calculator-server",
            version = "1.0.0"
        ),
        options = ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(true)
            )
        )
    )

    init {
        setupTools()
    }

    private fun setupTools() {
        // Add tool for addition
        server.addTool(
            name = "add",
            description = "Add two numbers together",
            inputSchema = ToolSchema(
                properties = buildJsonObject {
                    put("a", buildJsonObject {
                        put("type", "number")
                        put("description", "First number to add")
                    })
                    put("b", buildJsonObject {
                        put("type", "number")
                        put("description", "Second number to add")
                    })
                },
                required = listOf("a", "b")
            )
        ) { request ->
            val a = request.arguments?.get("a")?.toString()?.toDoubleOrNull()
            val b = request.arguments?.get("b")?.toString()?.toDoubleOrNull()

            if (a == null || b == null) {
                CallToolResult(
                    content = listOf(TextContent("Error: Both 'a' and 'b' must be valid numbers"))
                )
            } else {
                val result = a + b
                CallToolResult(
                    content = listOf(TextContent("Result: $a + $b = $result"))
                )
            }
        }

        // Add tool for subtraction
        server.addTool(
            name = "subtract",
            description = "Subtract the second number from the first number",
            inputSchema = ToolSchema(
                properties = buildJsonObject {
                    put("a", buildJsonObject {
                        put("type", "number")
                        put("description", "Number to subtract from")
                    })
                    put("b", buildJsonObject {
                        put("type", "number")
                        put("description", "Number to subtract")
                    })
                },
                required = listOf("a", "b")
            )
        ) { request ->
            val a = request.arguments?.get("a")?.toString()?.toDoubleOrNull()
            val b = request.arguments?.get("b")?.toString()?.toDoubleOrNull()

            if (a == null || b == null) {
                CallToolResult(
                    content = listOf(TextContent("Error: Both 'a' and 'b' must be valid numbers"))
                )
            } else {
                val result = a - b
                CallToolResult(
                    content = listOf(TextContent("Result: $a - $b = $result"))
                )
            }
        }

        // Add tool for multiplication
        server.addTool(
            name = "multiply",
            description = "Multiply two numbers together",
            inputSchema = ToolSchema(
                properties = buildJsonObject {
                    put("a", buildJsonObject {
                        put("type", "number")
                        put("description", "First number to multiply")
                    })
                    put("b", buildJsonObject {
                        put("type", "number")
                        put("description", "Second number to multiply")
                    })
                },
                required = listOf("a", "b")
            )
        ) { request ->
            val a = request.arguments?.get("a")?.toString()?.toDoubleOrNull()
            val b = request.arguments?.get("b")?.toString()?.toDoubleOrNull()

            if (a == null || b == null) {
                CallToolResult(
                    content = listOf(TextContent("Error: Both 'a' and 'b' must be valid numbers"))
                )
            } else {
                val result = a * b
                CallToolResult(
                    content = listOf(TextContent("Result: $a × $b = $result"))
                )
            }
        }

        // Add tool for division
        server.addTool(
            name = "divide",
            description = "Divide the first number by the second number",
            inputSchema = ToolSchema(
                properties = buildJsonObject {
                    put("a", buildJsonObject {
                        put("type", "number")
                        put("description", "Number to be divided (dividend)")
                    })
                    put("b", buildJsonObject {
                        put("type", "number")
                        put("description", "Number to divide by (divisor)")
                    })
                },
                required = listOf("a", "b")
            )
        ) { request ->
            val a = request.arguments?.get("a")?.toString()?.toDoubleOrNull()
            val b = request.arguments?.get("b")?.toString()?.toDoubleOrNull()

            if (a == null || b == null) {
                CallToolResult(
                    content = listOf(TextContent("Error: Both 'a' and 'b' must be valid numbers"))
                )
            } else if (b == 0.0) {
                CallToolResult(
                    content = listOf(TextContent("Error: Division by zero is not allowed"))
                )
            } else {
                val result = a / b
                CallToolResult(
                    content = listOf(TextContent("Result: $a ÷ $b = $result"))
                )
            }
        }
    }

    fun start() = runBlocking {
        val transport = StdioServerTransport(
            inputStream = System.`in`.asSource().buffered(),
            outputStream = System.out.asSink().buffered()
        )

        server.createSession(transport)

        // Keep the server running
        val done = kotlinx.coroutines.Job()
        server.onClose {
            done.complete()
        }
        done.join()
    }
}
