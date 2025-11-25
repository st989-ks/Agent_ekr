package com.application.agent_ekr.tools.examples

import com.application.agent_ekr.models.common.ToolDefinition
import com.application.agent_ekr.tools.Tool
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put

/**
 * Example weather tool implementation
 */
class WeatherTool : Tool {
    override val definition = ToolDefinition(
        name = "get_weather",
        description = "Get current weather for a location",
        parameters = buildJsonObject {
            put("location", buildJsonObject {
                put("type", "string")
                put("description", "City name")
            })
            put("unit", buildJsonObject {
                put("type", "string")
                put(
                    "enum",
                    JsonArray(listOf("celsius", "fahrenheit").map { Json.encodeToJsonElement(it) })
                )
                put("description", "Temperature unit")
            })
        }
    )

    // TODO: Implement actual weather API integration
    override suspend fun execute(arguments: String): String {
        val args = Json.decodeFromString<WeatherArgs>(arguments)
        // Simulate API call
        return """{
            "location": "${args.location}",
            "temperature": 22,
            "unit": "${args.unit}",
            "condition": "sunny"
        }""".trimIndent()
    }

    @Serializable
    private data class WeatherArgs(
        val location: String,
        val unit: String = "celsius"
    )
}