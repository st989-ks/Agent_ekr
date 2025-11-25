package com.application.agent_ekr.console

import com.application.agent_ekr.models.common.ToolDefinition
import org.slf4j.Logger

/**
 * ConsoleToolRegistry manages the registration and execution of console tools.
 * 
 * This class provides methods to register tools, retrieve their definitions,
 * and execute them with proper error handling and logging.
 */
class ConsoleToolRegistry(
    private val logger: Logger,
    private val config: ConsoleUIConfig
) {
    private val tools = mutableMapOf<String, ConsoleTool>()

    /**
     * Register a tool to be available for execution
     * 
     * @param tool The ConsoleTool implementation to register
     */
    fun registerTool(tool: ConsoleTool) {
        tools[tool.definition.name] = tool
        logger.debug("Registered tool: ${tool.definition.name}")
        if (config.debugMode) {
            println(ConsoleStyler.success("Registered tool: ${tool.definition.name}"))
        }
    }

    /**
     * Get a single tool definition by name
     * 
     * @param name The name of the tool to retrieve
     * @return ToolDefinition if found, null otherwise
     */
    fun getToolDefinition(name: String): ToolDefinition? {
        return tools[name]?.definition
    }

    /**
     * Get all registered tool definitions
     * 
     * @return List of all available tool definitions
     */
    fun getAllToolDefinitions(): List<ToolDefinition> {
        return tools.values.map { it.definition }
    }

    /**
     * Execute a tool by name with the provided arguments
     * 
     * @param name The name of the tool to execute
     * @param arguments JSON string containing the tool's input parameters
     * @return ToolExecutionResult indicating success or failure
     */
    suspend fun executeTool(name: String, arguments: String): ToolExecutionResult {
        val tool = tools[name]
        return if (tool != null) {
            try {
                if (config.debugMode) {
                    println(ConsoleStyler.debug("Executing tool: $name with arguments: $arguments"))
                }
                val result = tool.execute(arguments)
                ToolExecutionResult.Success(result)
            } catch (e: Exception) {
                logger.error("Error executing tool: $name", e)
                ToolExecutionResult.Error(name, "Execution failed: ${e.message}")
            }
        } else {
            logger.warn("Tool not found: $name")
            ToolExecutionResult.NotFound(name)
        }
    }

    /**
     * List all registered tools in a formatted string
     * 
     * @return Formatted string listing all tools, or "No tools registered" if empty
     */
    fun listTools(): String {
        return if (tools.isEmpty()) {
            "No tools registered"
        } else {
            tools.values.joinToString("\n") { tool ->
                "• ${tool.definition.name}: ${tool.definition.description}"
            }
        }
    }
}
