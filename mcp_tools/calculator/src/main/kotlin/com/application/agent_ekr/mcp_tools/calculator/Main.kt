package com.application.agent_ekr.mcp_tools.calculator

import com.application.agent_ekr.mcp_tools.calculator.McpServer.startSse
import com.application.agent_ekr.mcp_tools.calculator.McpServer.startStdio
import com.application.agent_ekr.mcp_tools.calculator.ToolCalculator.addDivision
import com.application.agent_ekr.mcp_tools.calculator.ToolCalculator.addMultiplication
import com.application.agent_ekr.mcp_tools.calculator.ToolCalculator.addPlus
import com.application.agent_ekr.mcp_tools.calculator.ToolCalculator.addSubtract
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ServerCapabilities

/**
 * Start sse-server mcp on port 3001.
 *
 * @param args
 * - "--stdio": Runs an MCP server using standard input/output.
 * - "--sse <port>": Runs an SSE MCP server.
 */
fun main(vararg args: String) {

    val command = args.firstOrNull() ?: "--stdio"
    val port = args.getOrNull(1)?.toIntOrNull() ?: 3001

    val server = Server(
        serverInfo = Implementation(
            name = "local-calculator",
            version = BuildConfig.VERSION,
        ),
        options  = ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true),
            ),
        ),
    ){
        addPlus()
        addSubtract()
        addMultiplication()
        addDivision()
    }

    when (command) {
        "--stdio" -> server.startStdio()
        "--sse" -> server.startSse(port).start(true)

        else -> {
            error("Unknown command: $command")
        }
    }
}