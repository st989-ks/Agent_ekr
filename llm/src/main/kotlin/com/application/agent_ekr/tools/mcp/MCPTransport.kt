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
                Runtime.getRuntime().exec(command.toTypedArray())
            }
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
