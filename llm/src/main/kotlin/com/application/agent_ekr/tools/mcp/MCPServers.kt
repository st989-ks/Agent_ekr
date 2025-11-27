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
}
