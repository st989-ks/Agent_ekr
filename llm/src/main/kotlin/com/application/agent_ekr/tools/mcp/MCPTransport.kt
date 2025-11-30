package com.application.agent_ekr.tools.mcp

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.modelcontextprotocol.kotlin.sdk.client.SseClientTransport
import io.modelcontextprotocol.kotlin.sdk.client.StdioClientTransport
import io.modelcontextprotocol.kotlin.sdk.client.mcpStreamableHttpTransport
import io.modelcontextprotocol.kotlin.sdk.shared.AbstractTransport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlin.time.Duration

sealed class MCPTransport {
    data class Stdio(
        val command: List<String>
    ) : MCPTransport()

    data class StreamableHttp(
        val url: String,
        val httpClient: HttpClient,
        val reconnectionTime: Duration? = null,
        val requestBuilder: HttpRequestBuilder.() -> Unit = {},
    ) : MCPTransport()

    data class Sse(
        val url: String,
        val httpClient: HttpClient,
        val reconnectionTime: Duration? = null,
        val requestBuilder: HttpRequestBuilder.() -> Unit = {},
    ) : MCPTransport()

    suspend fun createTransport(): AbstractTransport = when (this) {
        is Stdio -> {
            val process = withContext(Dispatchers.IO) {
                try {
                    ProcessBuilder(command).start()
                } catch (e: Exception) {
                    throw RuntimeException("Failed to start process '${command.joinToString(" ")}': ${e.message}", e)
                }
            }
            // Check if process started successfully
            if (!process.isAlive) {
                val exitCode = process.exitValue()
                throw RuntimeException("Process '${command.joinToString(" ")}' exited immediately with code $exitCode")
            }
            
            // Add a longer delay to ensure the process is ready and has printed any startup messages
            kotlinx.coroutines.delay(500)
            
            StdioClientTransport(
                input = process.inputStream.asSource().buffered(),
                output = process.outputStream.asSink().buffered()
            )
        }

        is StreamableHttp -> httpClient.mcpStreamableHttpTransport(
            url = url,
            reconnectionTime = reconnectionTime,
            requestBuilder = requestBuilder
        )

        is Sse -> SseClientTransport(
            client = httpClient,
            urlString = url,
            reconnectionTime = reconnectionTime,
            requestBuilder = requestBuilder
        )
    }
}
