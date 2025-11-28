package com.application.agent_ekr.tools

import com.application.agent_ekr.models.common.ToolDefinition
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import javax.swing.UIManager.put

/**
 * Simple calculator tool that provides basic arithmetic operations
 */
class CalculatorTool : ConsoleTool {
    override val definition: ToolDefinition
        get() = ToolDefinition(
            name = "calculator",
            description = "Perform basic arithmetic operations: addition, subtraction, multiplication, and division",
            parameters = buildJsonObject {
                put("operation", buildJsonObject {
                    put("type", "string")
                    put("enum", listOf("add", "subtract", "multiply", "divide"))
                    put("description", "The arithmetic operation to perform")
                })
                put("a", buildJsonObject {
                    put("type", "number")
                    put("description", "First number")
                })
                put("b", buildJsonObject {
                    put("type", "number")
                    put("description", "Second number")
                })
                put("required", buildJsonObject {
                    put("type", "string")
                    put("operation", listOf("a", "b"))
                    put("description", "required operation")
                })
            },
         )

    override suspend fun execute(arguments: String): String {
        // Parse the JSON arguments
        val json = kotlinx.serialization.json.Json.parseToJsonElement(arguments).jsonObject
        val operation = json["operation"]?.jsonPrimitive?.content
        val a = json["a"]?.jsonPrimitive?.doubleOrNull
        val b = json["b"]?.jsonPrimitive?.doubleOrNull
        
        if (operation == null || a == null || b == null) {
            return "Error: Missing required parameters (operation, a, b)"
        }
        
        return when (operation) {
            "add" -> "${a + b}"
            "subtract" -> "${a - b}"
            "multiply" -> "${a * b}"
            "divide" -> {
                if (b == 0.0) "Error: Division by zero"
                else "${a / b}"
            }
            else -> "Error: Unknown operation '$operation'"
        }
    }
}
