package com.application.agent_ekr.tools

import com.application.agent_ekr.models.common.ToolDefinition

/**
 * Universal tool execution interface for LLM function calling.
 */
interface Tool {
    val definition: ToolDefinition
    // TODO: Implement tool execution with proper error handling
    suspend fun execute(arguments: String): String
}