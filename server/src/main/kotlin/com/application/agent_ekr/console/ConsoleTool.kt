package com.application.agent_ekr.console

import com.application.agent_ekr.models.common.ToolDefinition

/**
 * ConsoleTool interface defines the contract for tools that can be executed by the console application.
 * 
 * Each tool must provide a definition and an execution method that processes input arguments
 * and returns a result string.
 */
interface ConsoleTool {
    /**
     * The tool definition containing name, description, and parameters schema
     */
    val definition: ToolDefinition
    
    /**
     * Executes the tool with the provided arguments
     * 
     * @param arguments JSON string containing the tool's input parameters
     * @return Result of the tool execution as a string
     */
    suspend fun execute(arguments: String): String
}
