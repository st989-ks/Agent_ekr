package com.application.agent_ekr.mcp_tools.calculator

import io.ktor.http.HttpMethod
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.cors.routing.CORS
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.server.mcp
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered

object McpServer {

    fun Server.startStdio() {
        try {
            val inputSource = System.`in`.asSource().buffered()
            val outputSink = System.out.asSink().buffered()

            val transport = StdioServerTransport(
                inputStream = inputSource,
                outputStream = outputSink
            )

            runBlocking {
                createSession(transport)
                val done = Job()
                onClose {
                    done.complete()
                }
                done.join()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun Server.startSse(port: Int): EmbeddedServer<*, *> {
        return embeddedServer(CIO, host = "127.0.0.1", port = port) {
            install(CORS) {
                allowMethod(HttpMethod.Options)
                allowMethod(HttpMethod.Get)
                allowMethod(HttpMethod.Post)
                allowMethod(HttpMethod.Delete)
                allowNonSimpleContentTypes = true
            }
            mcp {
                return@mcp this@startSse
            }
        }
    }
}
