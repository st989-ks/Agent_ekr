package com.application.agent_ekr.tools.mcp

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

object MCPServers {
    fun github(): MCPTransport.Stdio = 
        MCPTransport.Stdio(listOf("npx", "@modelcontextprotocol/server-github"))
    
    fun filesystem(): MCPTransport.Stdio = 
        MCPTransport.Stdio(listOf("npx", "@modelcontextprotocol/server-filesystem"))
    
    fun postgres(): MCPTransport.Stdio = 
        MCPTransport.Stdio(listOf("npx", "@modelcontextprotocol/server-postgres"))
    
    fun braveSearch(): MCPTransport.Stdio = 
        MCPTransport.Stdio(listOf("npx", "@modelcontextprotocol/server-brave-search"))
    
    fun sqlite(): MCPTransport.Stdio = 
        MCPTransport.Stdio(listOf("npx", "@modelcontextprotocol/server-sqlite"))
    
    fun http(url: String): MCPTransport.StreamableHttp = 
        MCPTransport.StreamableHttp(url, HttpClient(CIO))
    
    fun sse(url: String): MCPTransport.Sse = 
        MCPTransport.Sse(url, HttpClient(CIO))
    
    fun calculator(): MCPTransport.SseWithProcessBuilder {
        val  port = 3081
        return MCPTransport.SseWithProcessBuilder(
            command = listOf(
                "java",
                "-jar",
                "mcp_tools/calculator/build/libs/calculator-0.1.0.jar",
                "--sse"
            ),
            port = port,
            url = "http://localhost:$port",
            httpClient = HttpClient(CIO)
        )
    }
}
