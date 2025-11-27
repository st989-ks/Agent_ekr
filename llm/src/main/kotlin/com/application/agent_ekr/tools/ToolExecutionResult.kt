package com.application.agent_ekr.tools

/**
 * ToolExecutionResult represents the outcome of a tool execution.
 * 
 * This sealed class provides different result types for tool execution,
 * including success, not found, and error cases.
 */
sealed class ToolExecutionResult {
    /**
     * Represents successful tool execution with a result string
     */
    data class Success(val result: String) : ToolExecutionResult()
    
    /**
     * Represents when the requested tool is not found in the registry
     */
    data class NotFound(val toolName: String) : ToolExecutionResult()
    
    /**
     * Represents when tool execution failed with an error message
     */
    data class Error(val toolName: String, val error: String) : ToolExecutionResult()
}
